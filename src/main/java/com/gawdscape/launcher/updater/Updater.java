package com.gawdscape.launcher.updater;

import com.gawdscape.json.game.AssetIndex;
import com.gawdscape.json.game.Minecraft;
import com.gawdscape.json.modpacks.ModPack;
import com.gawdscape.launcher.Config;
import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.launcher.LauncherFrame;
import com.gawdscape.launcher.launch.MinecraftLauncher;
import com.gawdscape.launcher.util.*;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

/**
 *
 * @author Vinnie
 */
public class Updater extends Thread {

    private final String packName;
    private static ModPack modpack;
    private static Minecraft minecraft;
    private static AssetIndex assetIndex;
    private boolean newMcVer;

    public Updater(String packName) {
	this.packName = packName;
    }

    private void checkForUpdate() {
	long startTime = System.currentTimeMillis();
	GawdScapeLauncher.logger.info("Checking for game updates...");

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
		Utils.benchmark("Update check took {0}ms", startTime);
		launch();
	    } else {
		if (!modpack.getMinecraftVersion().equals(
			latestModPack.getMinecraftVersion())) {
		    newMcVer = true;
		}
		// Already downloaded, needs updating
		Utils.benchmark("Update check took {0}ms", startTime);
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
	    Utils.benchmark("Update check took {0}ms", startTime);
	    // No game, download now
	    modpack = downloadModPackData();
	    newMcVer = true;
	    update();
	}
    }

    private ModPack downloadModPackData() {
	// GawdScape
	try {
	    String json = JsonUtils.readJsonFromUrl(
		    GawdScapeLauncher.modpacks.getPackUrl(packName) + "/pack.json");
	    return JsonUtils.getGson().fromJson(json, ModPack.class);
	} catch (IOException ex) {
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Error loading " + packName + ".json", ex);
	    return null;
	}
    }

    private ModPack loadLocalModPackData(File jsonFile) {
	try {
	    String localJson = JsonUtils.readJsonFromFile(jsonFile);
	    return JsonUtils.getGson().fromJson(localJson, ModPack.class);
	} catch (IOException ex) {
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Error loading local " + packName + ".json", ex);
	    return null;
	}
    }

    private void saveModPackData() {
	try {
	    String json = JsonUtils.getGson().toJson(modpack);
	    JsonUtils.writeJsonToFile(json, new File(
		    Directories.getPackDataPath(), packName + ".json"));
	} catch (IOException ex) {
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Error saving " + packName + ".json", ex);
	}
    }

    private boolean loadLocalMinecraftData() {
	File minecraftJsonFile = new File(
		Directories.getMcJson(modpack.getMinecraftVersion()));
	// Load current minecraft.json
	try {
	    String localJson = JsonUtils.readJsonFromFile(minecraftJsonFile);
	    minecraft = JsonUtils.getGson().fromJson(localJson, Minecraft.class);
	} catch (IOException ex) {
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Error loading local Minecraft "
		    + modpack.getMinecraftVersion() + ".json", ex);
	    return false;
	}
	return true;
    }

    private boolean downloadMinecraftData() {
	// Minecraft
	String minecraftJson;
	try {
	    minecraftJson = JsonUtils.readJsonFromUrl(
		    Constants.getMcJson(modpack.getMinecraftVersion()));
	    minecraft = JsonUtils.getGson().fromJson(minecraftJson, Minecraft.class);
	} catch (IOException ex) {
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Error loading Minecraft "
		    + modpack.getMinecraftVersion() + ".json", ex);
	    return false;
	}
	try {
	    JsonUtils.writeJsonToFile(minecraftJson,
		    new File(Directories.getMcJson(modpack.getMinecraftVersion())));
	} catch (IOException ex) {
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Error saving Minecraft "
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
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Error loading Assets "
		    + minecraft.getAssets() + ".json", ex);
	    return false;
	}
	try {
	    JsonUtils.writeJsonToFile(assetJson,
		    new File(Directories.getAssetIndexPath(),
			    minecraft.getAssets() + ".json"));
	} catch (IOException ex) {
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Error saving Assets"
		    + minecraft.getAssets() + ".json", ex);
	}
	return true;
    }

    private int promptUpdate(String name, String version) {
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

    private void releaseNotes() {
	OperatingSystem.openLink(Constants.constantURI(
		GawdScapeLauncher.modpacks.getPackUrl(packName) + "/README.md"
	));
    }

    private void downloadMinecraft() {
	DownloadManager.mcVer = minecraft.getId();

	try {
	    FileUtils.delete(new File(
		    Directories.getNativesPath(minecraft.getId())));
	} catch (FileNotFoundException ex) {
	} catch (IOException ex) {
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Error deleting natives directory", ex);
	}

	DownloadManager.queueLibraries(minecraft.getRelevantLibraries());
	DownloadManager.queueMinecraft();
	DownloadManager.queueAssets(assetIndex);
    }

    private void update() {
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
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Error deleting mods directory", ex);
	}

	// Download Mod pack
	DownloadManager.queueLibraries(modpack.getRelevantLibraries());
	DownloadManager.queueMods(packName, modpack.getMods());
	if (modpack.getGawdModVersion() != null) {
	    DownloadManager.queueGawdMod(modpack.getMinecraftVersion(),
		    modpack.getGawdModVersion());
	}

	if (!DownloadManager.completeQueue()) {
	    // Close the download dialog
	    DownloadManager.downloadDialog.dispose();
	    DownloadManager.downloadDialog = null;
	    return;
	}

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

	Utils.benchmark("Updated Minecraft in {0}ms", startTime);
    }

    private void disableMods() {
	File modDir = new File(
		GawdScapeLauncher.config.getGameDir(packName), "bin");
	GawdScapeLauncher.logger.log(Level.INFO, "Disabling mods in folder {0}", modDir.getPath());
	if (!modDir.exists()) {
	    return;
	}
	for (File modFile : modDir.listFiles()) {
	    if (modFile.isFile()) {
		String modName = modFile.getName().toLowerCase();
		if (modName.endsWith(".jar") || modName.endsWith(".zip")) {
		    modName = modFile.getName();
		    modFile.renameTo(new File(modDir, modName + ".disabled"));
		    GawdScapeLauncher.logger.log(Level.INFO, "Disabled mod: {0}", modName);
		}
	    }
	}
    }

    private void launch() {
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
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Error loading Minecraft", ex);
	}
	Utils.benchmark("Launched Minecraft in {0}ms", startTime);
	GawdScapeLauncher.logger.info(
		"\n#==============================================================================#"
		+ "\n#--------------------------------- Minecraft ----------------------------------#"
		+ "\n#==============================================================================#");
    }

    private void updatePackLogo() {
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
	    GawdScapeLauncher.logger.info("Forcing update...");
	    modpack = downloadModPackData();
	    newMcVer = true;
	    update();
	} else {
	    checkForUpdate();
	}
    }
}
