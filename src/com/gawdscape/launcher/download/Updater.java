package com.gawdscape.launcher.download;

import com.gawdscape.launcher.Config;
import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.launcher.LauncherFrame;
import com.gawdscape.launcher.game.AssetIndex;
import com.gawdscape.launcher.game.ModPack;
import com.gawdscape.launcher.game.Minecraft;
import com.gawdscape.launcher.launch.MinecraftLauncher;
import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.FileUtils;
import com.gawdscape.launcher.util.JsonUtils;
import com.gawdscape.launcher.util.Log;
import com.gawdscape.launcher.util.OperatingSystem;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author Vinnie
 */
public class Updater extends Thread {

	public static String packName;
	public static ModPack modpack;
	public static Minecraft minecraft;
	public static AssetIndex assetIndex;
	public boolean newMcVer;

	public void checkForUpdate() {
		long startTime = System.currentTimeMillis();
		Log.info("Checking for game updates...");

		// Checking for gawdscape.json
		File gawdscapeJsonFile = new File(Directories.getBinPath(), packName + ".json");
		if (gawdscapeJsonFile.exists()) {
			// Load current gawdscape.json
			String localJson = "";
			try {
				localJson = JsonUtils.readJsonFromFile(gawdscapeJsonFile);
			} catch (Exception ex) {
				Log.error("Error loading local " + packName + ".json", ex);
			}
			ModPack localGawdScape = JsonUtils.getGson().fromJson(localJson, ModPack.class);

			// Download latest gawdscape.json
			downloadGawdScapeData();

			if (localGawdScape.getVersion().equals(modpack.getVersion())) {
				// Up to date, launch game
				long endTime = System.currentTimeMillis();
				Log.finest("Update check took " + (endTime - startTime) + "ms");
				launch();
			} else {
				if (!localGawdScape.getMinecraftVersion().equals(modpack.getMinecraftVersion())) {
					newMcVer = true;
				}
				// Already downloaded, needs updating
				long endTime = System.currentTimeMillis();
				Log.finest("Update check took " + (endTime - startTime) + "ms");
				promptUpdate();
			}
		} else {
			long endTime = System.currentTimeMillis();
			Log.finest("Update check took " + (endTime - startTime) + "ms");
			// No game, download now
			update();
		}
	}

	public void downloadGawdScapeData() {
		// GawdScape
		String gawdscapeJson = "";
		try {
			gawdscapeJson = JsonUtils.readJsonFromUrl(Constants.GS_PACK_URL + packName + "/pack.json");
		} catch (IOException ex) {
			Log.error("Error loading " + packName + ".json", ex);
		}
		modpack = JsonUtils.getGson().fromJson(gawdscapeJson, ModPack.class);
	}

	public void saveGawdScapeData() {
		String json = "";
		json = JsonUtils.getGson().toJson(modpack);
		try {
			JsonUtils.writeJsonToFile(json, new File(Directories.getBinPath(), packName + ".json"));
		} catch (IOException ex) {
			Log.error("Error saving " + packName + ".json", ex);
		}
	}

	public void loadLocalMinecraftData() {
		File minecraftJsonFile = new File(Directories.getBinPath(), "minecraft-" + modpack.getMinecraftVersion() + ".json");
		// Load current minecraft.json
		String localJson = "";
		try {
			localJson = JsonUtils.readJsonFromFile(minecraftJsonFile);
		} catch (Exception ex) {
			Log.error("Error loading local minecraft-" + modpack.getMinecraftVersion() + ".json", ex);
		}
		minecraft = JsonUtils.getGson().fromJson(localJson, Minecraft.class);
	}

	public void downloadMinecraftData() {
		// Minecraft
		String minecraftJson = "";
		try {
			minecraftJson = JsonUtils.readJsonFromUrl(Constants.MC_DOWNLOAD_URL + "versions/" + modpack.getMinecraftVersion() + "/" + modpack.getMinecraftVersion() + ".json");
		} catch (IOException ex) {
			Log.error("Error loading minecraft-" + modpack.getMinecraftVersion() + ".json", ex);
		}
		minecraft = JsonUtils.getGson().fromJson(minecraftJson, Minecraft.class);
		try {
			JsonUtils.writeJsonToFile(minecraftJson, new File(Directories.getBinPath(), "minecraft-" + modpack.getMinecraftVersion() + ".json"));
		} catch (IOException ex) {
			Log.error("Error saving minecraft-" + modpack.getMinecraftVersion() + ".json", ex);
		}

		// Assets
		String assetJson = "";
		try {
			assetJson = JsonUtils.readJsonFromUrl(Constants.MC_DOWNLOAD_URL + "indexes/" + minecraft.getAssets() + ".json");
		} catch (IOException ex) {
			Log.error("Error loading assets " + minecraft.getAssets() + ".json", ex);
		}
		assetIndex = JsonUtils.getGson().fromJson(assetJson, AssetIndex.class);
		try {
			JsonUtils.writeJsonToFile(assetJson, new File(Directories.getAssetIndexPath(), minecraft.getAssets() + ".json"));
		} catch (IOException ex) {
			Log.error("Error saving assets" + minecraft.getAssets() + ".json", ex);
		}
	}

	public void promptUpdate() {
		Object[] options = {"Yes", "No", "Release Notes"};
		int n = JOptionPane.showOptionDialog(GawdScapeLauncher.launcherFrame,
				modpack.getId() + " version " + modpack.getVersion()+ " has been released."
				+ "\nWould you like to download now?",
				"ModPack Update",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
		switch (n) {
			case JOptionPane.YES_OPTION:
				update();
				break;
			case JOptionPane.NO_OPTION:
				launch();
				break;
			case JOptionPane.CANCEL_OPTION:
				releaseNotes();
			default:
				LauncherFrame.playButton.setEnabled(true);
		}
	}

	public void releaseNotes() {
		OperatingSystem.openLink(Constants.constantURI(
				Constants.GS_PACK_FRONT_URL + modpack.getId() + "/README.md"
		));
	}

	public void update() {
		long startTime = System.currentTimeMillis();

		DownloadManager.createDialog(packName);

		downloadGawdScapeData();
		downloadMinecraftData();

		try {
			FileUtils.delete(new File(Directories.getNativesPath(minecraft.getId())));
		} catch (Exception ex) {
			Log.error("Error deleteing natives directory", ex);
		}
		try {
			FileUtils.delete(new File(GawdScapeLauncher.config.getGameDir(packName), "mods"));
		} catch (Exception ex) {
			Log.error("Error deleteing mods directory", ex);
		}

		downloadGame();

		// Extract Natives
		DownloadManager.extractNatives(minecraft.getId(), minecraft.getRelevantLibraries());

		// Remove META-INF so Minecraft can be modded
		DownloadManager.removeMinecraftMetaInf(minecraft.getId());

		// Save gawdscape.json to indicate we're up to date
		saveGawdScapeData();

		// Disable mods on MC version change
		if (newMcVer) {
			DownloadManager.downloadDialog.setDisableMods();
			disableMods();
		}

		DownloadManager.downloadDialog.setLaunching();
		Directories.createGameDirs(GawdScapeLauncher.config.getGameDir(packName));
		// Try to launch now
		launch();

		// Update the pack logo
		updatePackLogo();

		// Close the download dialog
		DownloadManager.downloadDialog.dispose();
		DownloadManager.downloadDialog = null;

		long endTime = System.currentTimeMillis();
		Log.finest("Updated Minecraft in " + (endTime - startTime) + "ms");
	}

	public void downloadGame() {
		// Queue all nessary game files
		DownloadManager.queueLibraries(minecraft.getRelevantLibraries(), modpack.getRelevantLibraries());
		DownloadManager.queueMinecraft(minecraft.getId(), modpack.getGawdModVersion());
		DownloadManager.queueAssets(assetIndex);
		DownloadManager.queueMods(packName, modpack.getMods());
		DownloadManager.completeQueue();
	}

	public void disableMods() {
		File modDir = new File(GawdScapeLauncher.config.getGameDir(packName), "bin");
		Log.info("Disabling mods in folder " + modDir.getPath());
		for (File modFile : modDir.listFiles()) {
			if (modFile.isFile()) {
				String modName = modFile.getName().toLowerCase();
				if (modName.endsWith(".jar") || modName.endsWith(".zip")) {
					modName = modFile.getName();
					modFile.renameTo(new File(modDir, modName + ".disabled"));
					Log.info("Disabled mod: " + modName);
				}
			}
		}
	}

	public void launch() {
		long startTime = System.currentTimeMillis();
		loadLocalMinecraftData();
		try {
			GawdScapeLauncher.launcher = new MinecraftLauncher();
			GawdScapeLauncher.launcher.launchGame(minecraft, modpack);
			GawdScapeLauncher.launcherFrame.dispose();
			GawdScapeLauncher.launcherFrame = null;
		} catch (IOException ex) {
			Log.error("Error loading Minecraft", ex);
		}
		long endTime = System.currentTimeMillis();
		Log.finest("Launched Minecraft in " + (endTime - startTime) + "ms");
	}

	public void updatePackLogo() {
		File logo = new File(Directories.getLogoPath(packName));
		FileUtils.downloadFile(Constants.GS_PACK_URL + packName + "/logo.png", logo);
	}

	public void setPack(String name) {
		packName = name;
	}

	@Override
	public void run() {
		setName("Updater");
		if (Config.forceUpdate) {
			Log.info("Forcing update...");
			update();
		} else {
			checkForUpdate();
		}
	}
}
