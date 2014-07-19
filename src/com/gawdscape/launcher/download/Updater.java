package com.gawdscape.launcher.download;

import com.gawdscape.launcher.Config;
import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.launcher.game.AssetIndex;
import com.gawdscape.launcher.game.GawdScape;
import com.gawdscape.launcher.game.Minecraft;
import com.gawdscape.launcher.launch.MinecraftLauncher;
import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.JsonUtils;
import com.gawdscape.launcher.util.Log;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author Vinnie
 */
public class Updater extends Thread {

    public static GawdScape gawdscape;
    public static Minecraft minecraft;
    public static AssetIndex assetIndex;

    public static void checkForUpdate() {
	Log.info("Checking for updates...");

	// Checking for gawdscape.json
	File gawdscapeJsonFile = new File(Directories.getBinPath(), "gawdscape.json");
	if (gawdscapeJsonFile.exists()) {
	    // Load current gawdscape.json
	    String localJson = "";
	    try {
		localJson = JsonUtils.readJsonFromFile(gawdscapeJsonFile);
	    } catch (Exception ex) {
		Log.error("Error loading loacal gawdscape.json", ex);
	    }
	    GawdScape localGawdScape = JsonUtils.getGson().fromJson(localJson, GawdScape.class);

	    // Download latest gawdscape.json
	    downloadGawdScapeData();

	    //Log.fine(minecraft.toString());
	    if (localGawdScape.getId().equals(gawdscape.getId())) {
		// Up to date, launch game
		launch();
	    } else {
		// Already downloaded, needs updating
		promptUpdate();
	    }
	} else {
	    // No game, download now
	    update();
	}

    }

    public static void downloadGawdScapeData() {
	// GawdScape
	String gawdscapeJson = "";
	try {
	    gawdscapeJson = JsonUtils.readJsonFromUrl(Constants.GS_DOWNLOAD_URL + "gawdscape/latest/latest.json");
	} catch (IOException ex) {
	    Log.error("Error loading gawdscape.json", ex);
	}
	gawdscape = JsonUtils.getGson().fromJson(gawdscapeJson, GawdScape.class);
	//Log.config(gawdscape.toString());
    }

    public static void saveGawdScapeData() {
	String gawdscapeJson = "";
	gawdscapeJson = JsonUtils.getGson().toJson(gawdscape);
	try {
	    JsonUtils.writeJsonToFile(gawdscapeJson, new File(Directories.getBinPath(), "gawdscape.json"));
	} catch (IOException ex) {
	    Log.error("Error saving gawdscape.json", ex);
	}
    }

    public static void loadLocalMinecraftData() {
	File minecraftJsonFile = new File(Directories.getBinPath(), "minecraft.json");
	// Load current minecraft.json
	String localJson = "";
	try {
	    localJson = JsonUtils.readJsonFromFile(minecraftJsonFile);
	} catch (Exception ex) {
	    Log.error("Error loading loacal minecraft.json", ex);
	}
	minecraft = JsonUtils.getGson().fromJson(localJson, Minecraft.class);
    }

    public static void downloadMinecraftData() {
	// Minecraft
	String minecraftJson = "";
	try {
	    minecraftJson = JsonUtils.readJsonFromUrl(Constants.MC_DOWNLOAD_URL + "versions/" + gawdscape.getMinecraftVersion() + "/" + gawdscape.getMinecraftVersion() + ".json");
	} catch (IOException ex) {
	    Log.error("Error loading minecraft.json", ex);
	}
	minecraft = JsonUtils.getGson().fromJson(minecraftJson, Minecraft.class);
	//Log.config(minecraft.toString());
	try {
	    JsonUtils.writeJsonToFile(minecraftJson, new File(Directories.getBinPath(), "minecraft.json"));
	} catch (IOException ex) {
	    Log.error("Error saving minecraft.json", ex);
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

    public static void promptUpdate() {
	int n = JOptionPane.showConfirmDialog(
		GawdScapeLauncher.launcherFrame,
		"GawdScape version " + gawdscape.getId() + " has been released."
		+ "\nWould you like to download now?",
		"GawdScape Update",
		JOptionPane.YES_NO_OPTION);
	if (n == 0) {
	    update();
	} else {
	    launch();
	}
    }

    public static void update() {
	downloadGawdScapeData();
	downloadMinecraftData();

	downloadGame();

	DownloadManager.completeQueue();

	// Extract Natives
	DownloadManager.extractNatives(minecraft.getRelevantLibraries());

	// Remove META-INF so Minecraft can be modded
	DownloadManager.removeMinecraftMetaInf();

	DownloadManager.downloadDialog.setLaunching();

	// Save gawdscape.json to indicate we're up to date
	saveGawdScapeData();

	// Try to launch now
	launch();

	// Remove the dialog
	DownloadManager.downloadDialog.dispose();
    }

    public static void downloadGame() {
	DownloadManager.createDialog();

	// Queue all nessary game files
	DownloadManager.queueLibraries(minecraft.getRelevantLibraries(), gawdscape.getRelevantLibraries());
	DownloadManager.queueMinecraft(minecraft.getId(), gawdscape.getId());
	DownloadManager.queueAssets(assetIndex);
    }

    public static void launch() {
	loadLocalMinecraftData();
	try {
	    MinecraftLauncher launcher = new MinecraftLauncher();
	    launcher.launchGame(minecraft, gawdscape);
	    GawdScapeLauncher.launcherFrame.dispose();
	} catch (IOException ex) {
	    Log.error("Error loading Minecraft", ex);
	}
    }

    @Override
    public void run() {
	if (Config.forceUpdate) {
	    Log.info("Forcing update...");
	    update();
	} else {
	    checkForUpdate();
	}
    }
}
