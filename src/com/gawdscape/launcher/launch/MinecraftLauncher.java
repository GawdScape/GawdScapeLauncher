package com.gawdscape.launcher.launch;

import com.gawdscape.launcher.Config;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.SwingUtilities;

import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.json.auth.SessionResponse;
import com.gawdscape.json.game.AssetIndex;
import com.gawdscape.json.modpacks.ModPack;
import com.gawdscape.json.game.Minecraft;
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

	private Config config;
	private SessionResponse session;

	public void launchGame(Minecraft mc, ModPack pack) throws IOException {
		Log.info("Prepare for launch...");
		config = GawdScapeLauncher.config;
		session = GawdScapeLauncher.session;
		if (mc == null) {
			Log.severe("Aborting launch; Minecraft version is null?");
			return;
		}

		if (pack == null) {
			Log.severe("Aborting launch; Mod Pack version is null?");
			return;
		}

		File nativeDir = new File(Directories.getNativesPath(mc.getId()));
		File gameDirectory = config.getGameDir(pack.getId());
		Log.info("Launching in " + gameDirectory);
		if (!gameDirectory.exists()) {
			if (!gameDirectory.mkdirs()) {
				Log.severe("Aborting launch; couldn't create game directory");
			}
		} else if (!gameDirectory.isDirectory()) {
			Log.severe("Aborting launch; game directory is not actually a directory");
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

		if ((session == null) || (session.getSelectedProfile() == null)) {
			processLauncher.addCommands("--demo");
		}

		if (config.getGlobalResourcePacks()) {
			processLauncher.addCommands("--resourcePackDir",
					new File(config.getGameDir(), "resourcepacks").getAbsolutePath());
		}

		if (config.getFullscreen()) {
			processLauncher.addCommands("--fullscreen");
		}

		if (config.getjoinServer()) {
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
			Log.println(
					"#==============================================================================#\n"
					+ "#--------------------------------- Minecraft ----------------------------------#\n"
					+ "#==============================================================================#");
			Log.info(process.toString());
			process.safeSetExitRunnable(this);
		} catch (IOException e) {
			Log.error("Couldn't launch game", e);
		}
	}

	private String[] getMinecraftArguments(String packName, Minecraft version, String gsArgs, File gameDirectory) {
		if (version.getMinecraftArguments() == null) {
			Log.severe("Can't run version, missing minecraftArguments");
			return null;
		}

		// TODO: Properly serialize properties. Priority: Low
		// There's probably a better way to do this... But fuck it.
		String userProperties = "";
		if (session.getUser() != null && session.getUser().getProperties() != null) {
			userProperties = session.getUser().getProperties().toString();
		}

		// Get Twitch Access Token
		String twitchToken = "";
		if (userProperties.contains("twitch_access_token")) {
			int start = userProperties.indexOf("twitch_access_token") + 30;
			twitchToken = String.format("\"twitch_access_token\":[\"%s\"]", userProperties.substring(start, start + 31));
		}

		Map<String, String> map = new HashMap();
		map.put("auth_access_token", session.getAccessToken());
		map.put("user_properties", String.format("{%s}", twitchToken));
		if (session.getSessionId() != null) {
			map.put("auth_session", session.getSessionId());
		} else {
			map.put("auth_session", "-");
		}
		if (session.getSelectedProfile().getId() != null) {
			map.put("auth_player_name", session.getSelectedProfile().getName());
			map.put("auth_uuid", session.getSelectedProfile().getId());
			map.put("user_type", session.getSelectedProfile().isLegacy() ? "legacy" : "mojang");
		} else {
			map.put("auth_player_name", "Player");
			map.put("auth_uuid", new UUID(0L, 0L).toString());
			map.put("user_type", "legacy");
		}
		map.put("profile_name", packName);
		map.put("version_name", version.getId());

		map.put("game_directory", gameDirectory.getAbsolutePath());
		map.put("game_assets", reconstructAssets(version.getAssets()).getAbsolutePath());

		map.put("assets_root", new File(Directories.getAssetPath()).getAbsolutePath());
		map.put("assets_index_name", version.getAssets());

		String mcArgs = version.getMinecraftArguments();
		if (gsArgs != null) {
			mcArgs = gsArgs;
		}
		String[] split = mcArgs.split(" ");
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

	private File getAssetObject(String assetVersion, String name) throws IOException {
		File assetsDir = new File(Directories.getAssetPath(), "assets");
		File indexDir = new File(assetsDir, "indexes");
		File objectsDir = new File(assetsDir, "objects");
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
			Log.warning("No assets index file " + virtualRoot + "; can't reconstruct assets");
			return virtualRoot;
		}
		AssetIndex index;
		try {
			index = JsonUtils.getGson().fromJson(JsonUtils.readJsonFromFile(indexFile), AssetIndex.class);
		} catch (IOException ex) {
			Log.error("Error loading asset index", ex);
			return virtualRoot;
		}
		if (index.isVirtual()) {
			Log.info("Reconstructing virtual assets folder at " + virtualRoot);
			index.getFileMap().entrySet().stream().forEach((entry) -> {
				File target = new File(virtualRoot, entry.getKey());
				File original = new File(new File(objectDir, (entry.getValue()).getHash().substring(0, 2)), (entry.getValue()).getHash());
				if (!target.isFile()) {
					try {
						FileUtils.copyFile(original, target);
					} catch (IOException ex) {
						Log.error("Error coping assets", ex);
					}
				}
			});

			try {
				JsonUtils.writeJsonToFile(new Date().toString(), new File(virtualRoot, ".lastused"));
			} catch (IOException ex) {
				Log.error("Error making lastUsed file", ex);
			}
		}
		return virtualRoot;
	}

	private String constructClassPath(Minecraft mc, ModPack pack) {
		StringBuilder result = new StringBuilder();
		// Libraries
		Collection<File> classPath = mc.getClassPath();
		classPath.addAll(pack.getClassPath());
		// Mods
		try {
			File modDir = new File(GawdScapeLauncher.config.getGameDir(pack.getId()), "bin");
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
		if (pack.getGawdModVersion() != null) {
			classPath.add(new File(Directories.getGawdModJar(
					pack.getGawdModVersion(), pack.getMinecraftVersion())));
		}
		// Minecraft
		classPath.add(new File(Directories.getMcJar(mc.getId())));
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
			if (config.getCloseLog()) {
				System.exit(0);
			}
		} else if (GawdScapeLauncher.logFrame != null) {
			Log.severe("Game ended with bad state (exit code " + exitCode + ")");
			SwingUtilities.invokeLater(() -> {
				Log.info("Ignoring visibility rule and showing log due to a game crash");
				GawdScapeLauncher.logFrame.setVisible(true);
			});
		}
		// You close log, what you know??
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
