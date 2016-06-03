package com.gawdscape.launcher.launch;

import com.gawdscape.json.auth.SessionResponse;
import com.gawdscape.json.game.AssetIndex;
import com.gawdscape.json.game.Minecraft;
import com.gawdscape.json.modpacks.ModPack;
import com.gawdscape.launcher.Config;
import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.FileUtils;
import com.gawdscape.launcher.util.JsonUtils;
import com.gawdscape.launcher.util.OperatingSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import javax.swing.SwingUtilities;

/**
 *
 * @author Vinnie
 */
public class MinecraftLauncher implements MinecraftExit {

    private Config config;
    private SessionResponse session;

    public void launchGame(Minecraft mc, ModPack pack) throws IOException {
	GawdScapeLauncher.LOGGER.info("Prepare for launch...");
	config = GawdScapeLauncher.config;
	session = GawdScapeLauncher.session;
	if (mc == null) {
	    GawdScapeLauncher.LOGGER.severe("Aborting launch; Minecraft version is null?");
	    return;
	}

	if (pack == null) {
	    GawdScapeLauncher.LOGGER.severe("Aborting launch; Mod Pack version is null?");
	    return;
	}

	File nativeDir = new File(Directories.getNativesPath(mc.getId()));
	File gameDirectory = config.getGameDir(pack.getId());
	GawdScapeLauncher.LOGGER.log(Level.INFO, "Launching in {0}", gameDirectory);
	if (!gameDirectory.exists()) {
	    if (!gameDirectory.mkdirs()) {
		GawdScapeLauncher.LOGGER.severe("Aborting launch; couldn't create game directory");
	    }
	} else if (!gameDirectory.isDirectory()) {
	    GawdScapeLauncher.LOGGER.severe("Aborting launch; game directory is not actually a directory");
	    return;
	}
	String javaPath = config.getCustomJava() ? config.getJavaPath() : null;
	ProcessLauncher processLauncher = new ProcessLauncher(javaPath, config.getMemory(), config.getJavaArgs());
	processLauncher.directory(gameDirectory);

	OperatingSystem os = OperatingSystem.getCurrentPlatform();
	if (os.equals(OperatingSystem.OSX)) {
	    processLauncher.addCommands("-Xdock:icon=" + getAssetObject(mc.getAssets(), "icons/minecraft.icns").getAbsolutePath(), "-Xdock:name=Minecraft");
	} else if (os.equals(OperatingSystem.WINDOWS)) {
	    processLauncher.addCommands("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump");
	}
	processLauncher.addCommands("-Djava.library.path=" + nativeDir.getAbsolutePath());
	// UTF-8 Mode
	processLauncher.addCommands("-Dfile.encoding=UTF-8");
        // Tell Forge to ignore META-INF
        processLauncher.addCommands("-Dfml.ignoreInvalidMinecraftCertificates=true");
	processLauncher.addCommands("-cp", constructClassPath(mc, pack));
	String mainClass = mc.getMainClass();
	if (pack.getMainClass() != null) {
	    mainClass = pack.getMainClass();
	}
	processLauncher.addCommands(mainClass);

	String[] args = getMinecraftArguments(pack.getId(), mc, pack.getMinecraftArguments(), gameDirectory);
	if (args == null) {
	    return;
	}
	processLauncher.addCommands(args);

	if (config.getGlobalResourcePacks()) {
	    processLauncher.addCommands("--resourcePackDir",
		    new File(config.getGameDir(), "resourcepacks").getAbsolutePath());
	}

	if (config.getFullscreen()) {
	    processLauncher.addCommands("--fullscreen");
	}

	if (config.getJoinServer()) {
	    String ip = config.getServerIP();
	    if (ip.contains(":")) {
		String[] socket = ip.split(":");
		processLauncher.addCommands("--server", socket[0]);
		processLauncher.addCommands("--port", socket[1]);
	    } else {
		processLauncher.addCommands("--server", ip);
	    }
	}

	if (config.getWindowSize()) {
	    processLauncher.addCommands("--width", config.getWindowWidth());
	    processLauncher.addCommands("--height", config.getWindowHeight());
	}
	try {
	    MinecraftProcess process = processLauncher.start();
	    GawdScapeLauncher.LOGGER.finer(process.toString());
	    process.safeSetExitRunnable(this);
	} catch (IOException e) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Couldn't launch game", e);
	}
    }

    private String[] getMinecraftArguments(String packName, Minecraft version, String gsArgs, File gameDirectory) {
	Map<String, String> replacements = new HashMap<>();
	if (version.getMinecraftArguments() == null) {
	    GawdScapeLauncher.LOGGER.severe("Can't run version, missing minecraftArguments");
	    return null;
	}

	if (session != null) {
	    // TODO: Properly serialize properties. Priority: Low
	    // There's probably a better way to do this... But fuck it.
	    String userProperties = "";
	    if (session.getUser() != null && session.getUser().getProperties() != null) {
		userProperties = session.getUser().getProperties().toString();
	    }

	    // Get Twitch Access Token
	    String twitchToken = "{}";
	    if (userProperties.contains("twitch_access_token")) {
		int start = userProperties.indexOf("twitch_access_token") + 30;
		twitchToken = String.format("{\"twitch_access_token\":[\"%s\"]}", userProperties.substring(start, start + 31));
	    }

	    replacements.put("auth_access_token", session.getAccessToken());
	    replacements.put("user_properties", twitchToken);
	    replacements.put("auth_session", session.getSessionId());
	    replacements.put("auth_player_name", session.getSelectedProfile().getName());
	    replacements.put("auth_uuid", session.getSelectedProfile().getId());
	    replacements.put("user_type", session.getSelectedProfile().isLegacy() ? "legacy" : "mojang");
	} else {
	    replacements.put("auth_session", "-");
	    replacements.put("auth_player_name", "Player");
	    replacements.put("auth_uuid", new UUID(0L, 0L).toString());
	    replacements.put("user_type", "legacy");
	    replacements.put("user_properties", "{}");
	}

	replacements.put("profile_name", packName);
	replacements.put("version_name", version.getId());
        replacements.put("version_type", packName);

	replacements.put("game_directory", gameDirectory.getAbsolutePath());
	replacements.put("game_assets", reconstructAssets(version.getAssets()).getAbsolutePath());

	replacements.put("assets_root", new File(Directories.getAssetPath()).getAbsolutePath());
	replacements.put("assets_index_name", version.getAssets());

	String mcArgs = version.getMinecraftArguments();
	if (gsArgs != null) {
	    mcArgs = gsArgs;
	}
	// Loop through the default arguments
	for (Map.Entry<String, String> replace : replacements.entrySet()) {
            mcArgs = mcArgs.replace("${" + replace.getKey() + "}", replace.getValue());
	}
	return mcArgs.split(" ");
    }

    private File getAssetObject(String assetVersion, String name) throws IOException {
	File indexDir = new File(Directories.getAssetIndexPath());
	File objectsDir = new File(Directories.getAssetObjectPath());
	File indexFile = new File(indexDir, assetVersion + ".json");
	AssetIndex index = JsonUtils.getGson().fromJson(JsonUtils.readJsonFromFile(indexFile), AssetIndex.class);
	String hash = (index.getFileMap().get(name)).getHash();
	return new File(objectsDir, hash.substring(0, 2) + "/" + hash);
    }

    private File reconstructAssets(String assetVersion) {
	File assetsDir = new File(Directories.getAssetPath());
	File indexDir = new File(Directories.getAssetIndexPath());
	File objectDir = new File(Directories.getAssetObjectPath());
	File indexFile = new File(indexDir, assetVersion + ".json");
	File virtualRoot = new File(new File(assetsDir, "virtual"), assetVersion);
	if (!indexFile.isFile()) {
	    GawdScapeLauncher.LOGGER.log(Level.WARNING, "No assets index file {0}; can''t reconstruct assets", virtualRoot);
	    return virtualRoot;
	}
	AssetIndex index;
	try {
	    index = JsonUtils.getGson().fromJson(JsonUtils.readJsonFromFile(indexFile), AssetIndex.class);
	} catch (IOException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Error loading asset index", ex);
	    return virtualRoot;
	}
	if (index.isVirtual()) {
	    GawdScapeLauncher.LOGGER.log(Level.INFO, "Reconstructing virtual assets folder at {0}", virtualRoot);
	    index.getFileMap().entrySet().stream().forEach((entry) -> {
		File target = new File(virtualRoot, entry.getKey());
		File original = new File(new File(objectDir, (entry.getValue()).getHash().substring(0, 2)), (entry.getValue()).getHash());
		if (!target.isFile()) {
		    try {
			FileUtils.copyFile(original, target);
		    } catch (IOException ex) {
			GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Error coping assets", ex);
		    }
		}
	    });

	    try {
		JsonUtils.writeJsonToFile(new Date().toString(), new File(virtualRoot, ".lastused"));
	    } catch (IOException ex) {
		GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Error making lastUsed file", ex);
	    }
	}
	return virtualRoot;
    }

    private String constructClassPath(Minecraft mc, ModPack pack) {
	ArrayList<File> classPath = new ArrayList<>();
        // User Mods
	try {
	    File modDir = new File(GawdScapeLauncher.config.getGameDir(pack.getId()), "bin");
	    if (!modDir.exists()) {
		modDir.mkdirs();
	    }
	    String[] mods = modDir.list();
	    for (String mod : mods) {
		if (mod.toLowerCase().endsWith(".jar") || mod.toLowerCase().endsWith(".zip")) {
		    classPath.add(new File(modDir, mod));
		    GawdScapeLauncher.LOGGER.log(Level.INFO, "Loaded Mod: {0}", mod);
		}
	    }

	} catch (Exception e) {
	    GawdScapeLauncher.LOGGER.severe("Error loading mods");
	}
	// Libraries
        classPath.addAll(pack.getClassPath());
        classPath.addAll(mc.getClassPath());
        // Texperience Mod
	if (pack.getTexperienceVersion()!= null) {
	    classPath.add(new File(Directories.getTexperienceJar(
		    pack.getTexperienceVersion(), pack.getMinecraftVersion())));
	}
	// Minecraft
	classPath.add(new File(Directories.getMcJar(mc.getId())));

        // Build command
        StringBuilder result = new StringBuilder();
	String separator = System.getProperty("path.separator");
	for (File file : classPath) {
	    if (!file.isFile()) {
		GawdScapeLauncher.LOGGER.log(Level.WARNING, "Classpath file not found: {0}", file);
	    }
	    if (result.length() > 0) {
		result.append(separator);
	    }
	    result.append(file.getAbsolutePath());
	}
	return result.toString();
    }

    @Override
    public void onMinecraftExit(MinecraftProcess process) {
        try {
            int exitCode = process.getExitCode();
            if (exitCode == 0) {
                GawdScapeLauncher.LOGGER.info("Game ended with no troubles detected (exit code 0)");
                if (config.getCloseLog()) {
                    System.exit(0);
                }
            } else if (GawdScapeLauncher.logFrame != null) {
                GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Game ended with bad state (exit code {0})", exitCode);
                SwingUtilities.invokeLater(() -> {
                    GawdScapeLauncher.LOGGER.info("Ignoring visibility rule and showing log due to a game crash");
                    GawdScapeLauncher.logFrame.setVisible(true);
                });
            }
        } catch (IllegalThreadStateException ex) {
            GawdScapeLauncher.LOGGER.warning("The Minecraft process has not yet exited.");
	}
    }

    public void cleanupSkinCache() {
	GawdScapeLauncher.LOGGER.info("Clearing cached skins...");
	File skinDir = new File(Directories.getAssetPath(), "skins");
	try {
	    FileUtils.delete(skinDir);
	} catch (IOException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Error deleting skin cache.", ex);
	}
    }
}
