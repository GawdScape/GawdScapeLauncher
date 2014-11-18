package com.gawdscape.launcher.launch;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.swing.SwingUtilities;

import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.launcher.download.Updater;
import com.gawdscape.launcher.game.AssetIndex;
import com.gawdscape.launcher.game.GawdScape;
import com.gawdscape.launcher.game.Minecraft;
import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.FileUtils;
import com.gawdscape.launcher.util.JsonUtils;
import com.gawdscape.launcher.util.Log;
import com.gawdscape.launcher.util.OperatingSystem;

/**
 *
 * @author Vinnie
 */
public class MinecraftLauncher implements MinecraftExit {

    private final Object lock = new Object();
    private boolean isWorking;

    public void launchGame(Minecraft mc, GawdScape gs) throws IOException {
	Log.info("Prepare for launch...");
	if (mc == null) {
	    Log.severe("Aborting launch; Minecraft version is null?");
	    return;
	}

	if (gs == null) {
	    Log.severe("Aborting launch; GawdScape version is null?");
	    return;
	}

	File nativeDir = new File(Directories.getNativesPath());
	File assetsDir = new File(Directories.getAssetPath());
	File gameDirectory = GawdScapeLauncher.config.getGameDirectory();
	Log.info("Launching in " + gameDirectory);
	if (!gameDirectory.exists()) {
	    if (!gameDirectory.mkdirs()) {
		Log.severe("Aborting launch; couldn't create game directory");
	    }
	} else if (!gameDirectory.isDirectory()) {
	    Log.severe("Aborting launch; game directory is not actually a directory");
	    return;
	}
	ProcessLauncher processLauncher = new ProcessLauncher(null, new String[0]);
	processLauncher.directory(gameDirectory);

	OperatingSystem os = OperatingSystem.getCurrentPlatform();
	if (os.equals(OperatingSystem.OSX)) {
	    processLauncher.addCommands(new String[]{"-Xdock:icon=" + getAssetObject("icons/minecraft.icns").getAbsolutePath(), "-Xdock:name=Minecraft"});
	} else if (os.equals(OperatingSystem.WINDOWS)) {
	    processLauncher.addCommands(new String[]{"-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump"});
	}
	boolean is32Bit = "32".equals(System.getProperty("sun.arch.data.model"));
	String memoryArgument = is32Bit ? "-Xmx512M" : "-Xmx" + GawdScapeLauncher.config.getMemory() + "M";
	processLauncher.addSplitCommands(memoryArgument);
	processLauncher.addCommands(new String[]{"-Djava.library.path=" + nativeDir.getAbsolutePath()});
	processLauncher.addCommands(new String[]{"-cp", constructClassPath(mc, gs)});
	processLauncher.addCommands(new String[]{Updater.minecraft.getMainClass()});

	String[] args = getMinecraftArguments(Updater.minecraft, gameDirectory, assetsDir);
	if (args == null) {
	    return;
	}
	processLauncher.addCommands(args);

	if ((GawdScapeLauncher.response == null) || (GawdScapeLauncher.response.getSelectedProfile() == null)) {
	    processLauncher.addCommands(new String[]{"--demo"});
	}

	if (GawdScapeLauncher.config.getFullscreen()) {
	    processLauncher.addCommands(new String[]{"--fullscreen"});
	}

	if (GawdScapeLauncher.config.getjoinServer()) {
	    String ip = GawdScapeLauncher.config.getServerIP();
	    if (ip.contains(":")) {
		String[] socket = ip.split(":");
		processLauncher.addCommands(new String[]{"--server", String.valueOf(socket[0])});
		processLauncher.addCommands(new String[]{"--port", String.valueOf(socket[1])});
	    } else {
		processLauncher.addCommands(new String[]{"--server", String.valueOf(ip)});
	    }
	}

	if (GawdScapeLauncher.config.getWindowSize()) {
	    processLauncher.addCommands(new String[]{"--width", String.valueOf(GawdScapeLauncher.config.getWindowWidth())});
	    processLauncher.addCommands(new String[]{"--height", String.valueOf(GawdScapeLauncher.config.getWindowHeight())});
	}
	try {
	    MinecraftProcess process = processLauncher.start();
	    Log.info(process.toString());
            Log.println(
"#==============================================================================#\n" + 
"#--------------------------------- Minecraft ----------------------------------#\n" + 
"#==============================================================================#");
	    process.safeSetExitRunnable(this);
	} catch (IOException e) {
	    Log.error("Couldn't launch game", e);
	    return;
	}
    }

    private String[] getMinecraftArguments(Minecraft version, File gameDirectory, File assetsDirectory) {
	if (version.getMinecraftArguments() == null) {
	    Log.severe("Can't run version, missing minecraftArguments");
	    return null;
	}

	// There's probably a better way to do this... But fuck it.
	String userProperties = "";
	if (GawdScapeLauncher.response.getUser().getProperties() != null) {
	    userProperties = GawdScapeLauncher.response.getUser().getProperties().toString();
	}

	// Get Twitch Access Token
	String twitchToken = "";
	if (userProperties.contains("twitch_access_token")) {
	    int start = userProperties.indexOf("twitch_access_token") + 30;
	    twitchToken = String.format("\"twitch_access_token\":[\"%s\"]", userProperties.substring(start, start + 31));
	}

	Map<String, String> map = new HashMap();
	map.put("auth_access_token", GawdScapeLauncher.response.getAccessToken());
	map.put("user_properties", String.format("{%s}", twitchToken));
	if (GawdScapeLauncher.response.getSessionId() != null) {
	    map.put("auth_session", GawdScapeLauncher.response.getSessionId());
	} else {
	    map.put("auth_session", "-");
	}
	if (GawdScapeLauncher.response.getSelectedProfile().getId() != null) {
	    map.put("auth_player_name", GawdScapeLauncher.response.getSelectedProfile().getName());
	    map.put("auth_uuid", GawdScapeLauncher.response.getSelectedProfile().getId());
	    map.put("user_type", GawdScapeLauncher.response.getSelectedProfile().isLegacy() ? "legacy" : "mojang");
	} else {
	    map.put("auth_player_name", "Player");
	    map.put("auth_uuid", new UUID(0L, 0L).toString());
	    map.put("user_type", "legacy");
	}
	map.put("profile_name", "GawdScape");
	map.put("version_name", version.getId());

	map.put("game_directory", gameDirectory.getAbsolutePath());
	map.put("game_assets", reconstructAssets().getAbsolutePath());

	map.put("assets_root", new File(Directories.getAssetPath()).getAbsolutePath());
	map.put("assets_index_name", version.getAssets());

	String[] split = version.getMinecraftArguments().split(" ");
	// Loop through the default arguments
	for (int i = 0; i < split.length; i++) {
	    // Key = current command
	    String key = split[i];
	    // Remove the ${ } from the var
	    key = key.substring(2, key.length() - 1);
	    // Replace the string if it's a variable
	    if (map.containsKey(key)) {
		split[i] = map.get(key);
	    }
	}
	return split;
    }

    private File getAssetObject(String name) throws IOException {
	File assetsDir = new File(Directories.getAssetPath(), "assets");
	File indexDir = new File(assetsDir, "indexes");
	File objectsDir = new File(assetsDir, "objects");
	String assetVersion = Updater.minecraft.getAssets();
	File indexFile = new File(indexDir, assetVersion + ".json");
	AssetIndex index = (AssetIndex) JsonUtils.getGson().fromJson(JsonUtils.readJsonFromFile(indexFile), AssetIndex.class);

	String hash = ((AssetIndex.AssetObject) index.getFileMap().get(name)).getHash();
	return new File(objectsDir, hash.substring(0, 2) + "/" + hash);
    }

    private File reconstructAssets() {
	File assetsDir = new File(Directories.getAssetPath());
	File indexDir = new File(Directories.getAssetIndexPath());
	File objectDir = new File(Directories.getAssetObjectPath());
	String assetVersion = Updater.minecraft.getAssets();
	File indexFile = new File(indexDir, assetVersion + ".json");
	File virtualRoot = new File(new File(assetsDir, "virtual"), assetVersion);
	if (!indexFile.isFile()) {
	    Log.warning("No assets index file " + virtualRoot + "; can't reconstruct assets");
	    return virtualRoot;
	}
	AssetIndex index = null;
	try {
	    index = (AssetIndex) JsonUtils.getGson().fromJson(JsonUtils.readJsonFromFile(indexFile), AssetIndex.class);
	} catch (IOException ex) {
	    Log.error("Error loading asset index", ex);
	}
	if (index.isVirtual()) {
	    Log.info("Reconstructing virtual assets folder at " + virtualRoot);
	    for (Map.Entry<String, AssetIndex.AssetObject> entry : index.getFileMap().entrySet()) {
		File target = new File(virtualRoot, (String) entry.getKey());
		File original = new File(new File(objectDir, ((AssetIndex.AssetObject) entry.getValue()).getHash().substring(0, 2)), ((AssetIndex.AssetObject) entry.getValue()).getHash());
		if (!target.isFile()) {
		    try {
			FileUtils.copyFile(original, target);
		    } catch (IOException ex) {
			Log.error("Error coping assets", ex);
		    }
		}
	    }

	    try {
		JsonUtils.writeJsonToFile(new Date().toString(), new File(virtualRoot, ".lastused"));
	    } catch (IOException ex) {
		Log.error("Error making lastUsed file", ex);
	    }
	}
	return virtualRoot;
    }

    private String constructClassPath(Minecraft mc, GawdScape gs) {
	StringBuilder result = new StringBuilder();
	// Libraries
	Collection<File> classPath = mc.getClassPath();
	classPath.addAll(gs.getClassPath());
	// GawdScape
	classPath.add(new File(Directories.getBinPath(), "gawdscape.jar"));
	// Mods
	try {
	    File modDir = new File(Directories.getModPath());
	    if (!modDir.exists()) {
		modDir.mkdir();
	    }
	    String[] mods = modDir.list();
	    for (String mod : mods) {
		if (mod.toLowerCase().endsWith(".jar") || mod.toLowerCase().endsWith(".zip")) {
		    classPath.add(new File(modDir, mod));
		    Log.info("Loaded Mod: " + mod);
		}
	    }
	} catch (Exception e) {
	    Log.severe("Error loading mods");
	}
	// Minecraft
	classPath.add(new File(Directories.getBinPath(), "minecraft.jar"));
	String separator = System.getProperty("path.separator");
	for (File file : classPath) {
	    if (!file.isFile()) {
		throw new RuntimeException("Classpath file not found: " + file);
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
	int exitCode = process.getExitCode();
	if (exitCode == 0) {
	    Log.info("Game ended with no troubles detected (exit code " + exitCode + ")");
	    if (GawdScapeLauncher.config.getCloseLog()) {
		System.exit(0);
	    }
	} else if (GawdScapeLauncher.logFrame != null) {
	    Log.severe("Game ended with bad state (exit code " + exitCode + ")");
	    SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    Log.info("Ignoring visibility rule and showing log due to a game crash");
		    GawdScapeLauncher.logFrame.setVisible(true);
		}
	    });
	}
	// You close log, you don't know
    }

    public void cleanupSkinCache() {
	Log.info("Clearing cached skins...");
	File skinDir = new File(Directories.getAssetPath(), "skins");
	try {
	    FileUtils.delete(skinDir);
	} catch (IOException ex) {
	    Log.error("Error deleting skin cache.", ex);
	}
    }
}
