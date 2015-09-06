package com.gawdscape.launcher.updater;

import com.gawdscape.launcher.Config;
import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.launcher.LauncherFrame;
import com.gawdscape.json.game.AssetIndex;
import com.gawdscape.json.modpacks.ModPack;
import com.gawdscape.json.game.Minecraft;
import com.gawdscape.launcher.launch.MinecraftLauncher;
import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.FileUtils;
import com.gawdscape.launcher.util.JsonUtils;
import com.gawdscape.launcher.util.Log;
import com.gawdscape.launcher.util.OperatingSystem;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author Vinnie
 */
public class Updater extends Thread {

	private final String packName;
	public static ModPack modpack;
	public static Minecraft minecraft;
	public static AssetIndex assetIndex;
	public boolean newMcVer;

	public Updater(String packName) {
		this.packName = packName;
	}

	public void checkForUpdate() {
		long startTime = System.currentTimeMillis();
		Log.info("Checking for game updates...");

		// Checking for gawdscape.json
		File packDataFile = new File(
				Directories.getPackDataPath(), packName + ".json");
		if (packDataFile.exists()) {
			modpack = loadLocalModPackData(packDataFile);

			// Load current gawdscape.json
			ModPack latestModPack = downloadModPackData();

			// Download latest gawdscape.json
			if (latestModPack == null) {
				launch();
				return;
			}

			if (modpack.getVersion().equals(latestModPack.getVersion())) {
				// Up to date, launch game
				Log.benchmark("Update check took ", startTime);
				launch();
			} else {
				if (!modpack.getMinecraftVersion().equals(
						latestModPack.getMinecraftVersion())) {
					newMcVer = true;
				}
				// Already downloaded, needs updating
				Log.benchmark("Update check took ", startTime);
				switch (promptUpdate(packName, latestModPack.getVersion())) {
					case JOptionPane.YES_OPTION:
						modpack = latestModPack;
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
		} else {
			Log.benchmark("Update check took ", startTime);
			// No game, download now
			modpack = downloadModPackData();
			newMcVer = true;
			update();
		}
	}

	public ModPack downloadModPackData() {
		// GawdScape
		try {
			String json = JsonUtils.readJsonFromUrl(
					GawdScapeLauncher.modpacks.getPackUrl(packName) + "/pack.json");
			return JsonUtils.getGson().fromJson(json, ModPack.class);
		} catch (IOException ex) {
			Log.error("Error loading " + packName + ".json", ex);
			return null;
		}
	}

	public ModPack loadLocalModPackData(File jsonFile) {
		try {
			String localJson = JsonUtils.readJsonFromFile(jsonFile);
			return JsonUtils.getGson().fromJson(localJson, ModPack.class);
		} catch (IOException ex) {
			Log.error("Error loading local " + packName + ".json", ex);
			return null;
		}
	}

	public void saveModPackData() {
		try {
			String json = JsonUtils.getGson().toJson(modpack);
			JsonUtils.writeJsonToFile(json, new File(
					Directories.getPackDataPath(), packName + ".json"));
		} catch (IOException ex) {
			Log.error("Error saving " + packName + ".json", ex);
		}
	}

	public boolean loadLocalMinecraftData() {
		File minecraftJsonFile = new File(
				Directories.getMcJson(modpack.getMinecraftVersion()));
		// Load current minecraft.json
		try {
			String localJson = JsonUtils.readJsonFromFile(minecraftJsonFile);
			minecraft = JsonUtils.getGson().fromJson(localJson, Minecraft.class);
		} catch (IOException ex) {
			Log.error("Error loading local Minecraft "
					+ modpack.getMinecraftVersion() + ".json", ex);
			return false;
		}
		return true;
	}

	public boolean downloadMinecraftData() {
		// Minecraft
		String minecraftJson;
		try {
			minecraftJson = JsonUtils.readJsonFromUrl(
					Constants.getMcJson(modpack.getMinecraftVersion()));
			minecraft = JsonUtils.getGson().fromJson(minecraftJson, Minecraft.class);
		} catch (IOException ex) {
			Log.error("Error loading Minecraft "
					+ modpack.getMinecraftVersion() + ".json", ex);
			return false;
		}
		try {
			JsonUtils.writeJsonToFile(minecraftJson,
					new File(Directories.getMcJson(modpack.getMinecraftVersion())));
		} catch (IOException ex) {
			Log.error("Error saving Minecraft "
					+ modpack.getMinecraftVersion() + ".json", ex);
		}

		// Assets
		String assetJson;
		try {
			assetJson = JsonUtils.readJsonFromUrl(
					Constants.MC_DOWNLOAD_URL + "indexes/"
					+ minecraft.getAssets() + ".json");
			assetIndex = JsonUtils.getGson().fromJson(assetJson, AssetIndex.class);
		} catch (IOException ex) {
			Log.error("Error loading Assets "
					+ minecraft.getAssets() + ".json", ex);
			return false;
		}
		try {
			JsonUtils.writeJsonToFile(assetJson,
					new File(Directories.getAssetIndexPath(),
							minecraft.getAssets() + ".json"));
		} catch (IOException ex) {
			Log.error("Error saving Assets"
					+ minecraft.getAssets() + ".json", ex);
		}
		return true;
	}

	public int promptUpdate(String name, String version) {
		Object[] options = {"Yes", "No", "Release Notes"};
		return JOptionPane.showOptionDialog(GawdScapeLauncher.launcherFrame,
				name + " version " + version + " has been released."
				+ "\nWould you like to download now?",
				"ModPack Update",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
	}

	public void releaseNotes() {
		OperatingSystem.openLink(Constants.constantURI(
				GawdScapeLauncher.modpacks.getPackUrl(packName) + "/README.md"
		));
	}

	public void downloadMinecraft() {
		DownloadManager.mcVer = minecraft.getId();

		try {
			FileUtils.delete(new File(
					Directories.getNativesPath(minecraft.getId())));
		} catch (FileNotFoundException ex) {
		} catch (IOException ex) {
			Log.error("Error deleting natives directory", ex);
		}

		DownloadManager.queueLibraries(minecraft.getRelevantLibraries());
		DownloadManager.queueMinecraft();
		DownloadManager.queueAssets(assetIndex);
	}

	public void update() {
		long startTime = System.currentTimeMillis();

		DownloadManager.createDialog(packName);

		if (modpack == null) {
			return;
		}

		// Download Minecraft if necessary
		if (newMcVer) {
			disableMods();

			if (!downloadMinecraftData()) {
				return;
			}
			downloadMinecraft();
		}

		// Delete modpack mods
		try {
			FileUtils.delete(new File(
					GawdScapeLauncher.config.getGameDir(packName), "mods"));
		} catch (FileNotFoundException ex) {
		} catch (IOException ex) {
			Log.error("Error deleting mods directory", ex);
		}

		// Download Mod pack
		DownloadManager.queueLibraries(modpack.getRelevantLibraries());
		DownloadManager.queueMods(packName, modpack.getMods());
		if (modpack.getGawdModVersion() != null) {
			DownloadManager.queueGawdMod(modpack.getMinecraftVersion(),
					modpack.getGawdModVersion());
		}

		DownloadManager.completeQueue();

		// Save gawdscape.json to indicate we're up to date
		saveModPackData();

		java.awt.EventQueue.invokeLater(DownloadManager.downloadDialog::setLaunching);
		Directories.createGameDirs(
				GawdScapeLauncher.config.getGameDir(packName));
		// Try to launch now
		launch();

		// Update the pack logo
		updatePackLogo();

		// Close the download dialog
		DownloadManager.downloadDialog.dispose();
		DownloadManager.downloadDialog = null;

		Log.benchmark("Updated Minecraft in ", startTime);
	}

	public void disableMods() {
		File modDir = new File(
				GawdScapeLauncher.config.getGameDir(packName), "bin");
		Log.info("Disabling mods in folder " + modDir.getPath());
		if (!modDir.exists()) {
			return;
		}
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
		if (!loadLocalMinecraftData()) {
			return;
		}
		try {
			GawdScapeLauncher.launcher = new MinecraftLauncher();
			GawdScapeLauncher.launcher.launchGame(minecraft, modpack);
			if (GawdScapeLauncher.launcherFrame != null) {
				GawdScapeLauncher.launcherFrame.dispose();
			}
			GawdScapeLauncher.launcherFrame = null;
		} catch (IOException ex) {
			Log.error("Error loading Minecraft", ex);
		}
		Log.benchmark("Launched Minecraft in ", startTime);
		Log.println(
				"#==============================================================================#\n"
				+ "#--------------------------------- Minecraft ----------------------------------#\n"
				+ "#==============================================================================#");
	}

	public void updatePackLogo() {
		File logo = new File(Directories.getLogoPath(packName));
		FileUtils.downloadFile(
				GawdScapeLauncher.modpacks.getPackUrl(packName) + "/logo.png", logo);
	}

	@Override
	public void run() {
		setName("Updater");
		if (GawdScapeLauncher.offlineMode) {
			modpack = loadLocalModPackData(new File(
					Directories.getPackDataPath(), packName + ".json"));
			launch();
			return;
		}
		if (Config.forceUpdate) {
			Log.info("Forcing update...");
			modpack = downloadModPackData();
			newMcVer = true;
			update();
		} else {
			checkForUpdate();
		}
	}
}
