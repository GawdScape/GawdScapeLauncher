package com.gawdscape.launcher.download;

import com.gawdscape.launcher.Config;
import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.launcher.LauncherFrame;
import com.gawdscape.launcher.game.AssetIndex;
import com.gawdscape.launcher.game.GawdScape;
import com.gawdscape.launcher.game.Minecraft;
import com.gawdscape.launcher.launch.MinecraftLauncher;
import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.Directories;
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

    public static GawdScape gawdscape;
    public static Minecraft minecraft;
    public static AssetIndex assetIndex;
    public boolean disableMods;

    public void checkForUpdate() {
        long startTime = System.currentTimeMillis();
	Log.info("Checking for game updates...");

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

	    if (localGawdScape.getId().equals(gawdscape.getId())) {
		// Up to date, launch game
                long endTime = System.currentTimeMillis();
                Log.finest("Update check took " + (endTime-startTime) + "ms");
		launch();
	    } else {
                if(!localGawdScape.getMinecraftVersion().equals(gawdscape.getMinecraftVersion())) {
                    disableMods = true;
                }
		// Already downloaded, needs updating
                long endTime = System.currentTimeMillis();
                Log.finest("Update check took " + (endTime-startTime) + "ms");
		promptUpdate();
	    }
	} else {
            long endTime = System.currentTimeMillis();
            Log.finest("Update check took " + (endTime-startTime) + "ms");
	    // No game, download now
	    update();
	}
    }

    public void downloadGawdScapeData() {
	// GawdScape
	String gawdscapeJson = "";
	try {
	    gawdscapeJson = JsonUtils.readJsonFromUrl(Constants.GS_VERSION_URL);
	} catch (IOException ex) {
	    Log.error("Error loading gawdscape.json", ex);
	}
	gawdscape = JsonUtils.getGson().fromJson(gawdscapeJson, GawdScape.class);
    }

    public void saveGawdScapeData() {
	String gawdscapeJson = "";
	gawdscapeJson = JsonUtils.getGson().toJson(gawdscape);
	try {
	    JsonUtils.writeJsonToFile(gawdscapeJson, new File(Directories.getBinPath(), "gawdscape.json"));
	} catch (IOException ex) {
	    Log.error("Error saving gawdscape.json", ex);
	}
    }

    public void loadLocalMinecraftData() {
	File minecraftJsonFile = new File(Directories.getBinPath(), "minecraft.json");
	// Load current minecraft.json
	String localJson = "";
	try {
	    localJson = JsonUtils.readJsonFromFile(minecraftJsonFile);
	} catch (Exception ex) {
	    Log.error("Error loading local minecraft.json", ex);
	}
	minecraft = JsonUtils.getGson().fromJson(localJson, Minecraft.class);
    }

    public void downloadMinecraftData() {
	// Minecraft
	String minecraftJson = "";
	try {
	    minecraftJson = JsonUtils.readJsonFromUrl(Constants.MC_DOWNLOAD_URL + "versions/" + gawdscape.getMinecraftVersion() + "/" + gawdscape.getMinecraftVersion() + ".json");
	} catch (IOException ex) {
	    Log.error("Error loading minecraft.json", ex);
	}
	minecraft = JsonUtils.getGson().fromJson(minecraftJson, Minecraft.class);
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

    public void promptUpdate() {
		Object[] options = {"Yes", "No", "Release Notes"};
		int n = JOptionPane.showOptionDialog(
			GawdScapeLauncher.launcherFrame,
			"GawdScape version " + gawdscape.getId() + " has been released."
			+ "\nWould you like to download now?",
			"GawdScape Update",
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
		OperatingSystem.openLink(
			Constants.constantURI(
				Constants.GS_VERSION_NOTES_URL + gawdscape.getId()
			)
		);
    }

    public void update() {
        long startTime = System.currentTimeMillis();

        DownloadManager.createDialog();

        downloadGawdScapeData();
        downloadMinecraftData();

        downloadGame();

        // Extract Natives
        DownloadManager.extractNatives(minecraft.getRelevantLibraries());

        // Remove META-INF so Minecraft can be modded
        DownloadManager.removeMinecraftMetaInf();

        // Save gawdscape.json to indicate we're up to date
        saveGawdScapeData();

        // Disable mods on MC version change
        if (disableMods) {
            DownloadManager.downloadDialog.setDisableMods();
            disableMods();
        }

        DownloadManager.downloadDialog.setLaunching();
	Directories.createGameDirs(GawdScapeLauncher.config.getGameDirectory());
        // Try to launch now
        launch();

        // Close the download dialog
        DownloadManager.downloadDialog.dispose();
        DownloadManager.downloadDialog = null;

        long endTime = System.currentTimeMillis();
        Log.finest("Updated Minecraft in " + (endTime-startTime) + "ms");
    }

    public void downloadGame() {
		// Queue all nessary game files
		DownloadManager.queueLibraries(minecraft.getRelevantLibraries(), gawdscape.getRelevantLibraries());
		DownloadManager.queueMinecraft(minecraft.getId(), gawdscape.getId());
		DownloadManager.queueAssets(assetIndex);
        DownloadManager.completeQueue();
    }

    public void disableMods() {
        Log.info("Disabling mods in bin/mods folder...");
        File modDir = new File(Directories.getModPath());
        for (File modFile : modDir.listFiles()) {
            if (modFile.isFile()) {
                String modName = modFile.getName();
		if (modName.toLowerCase().endsWith(".jar") || modName.toLowerCase().endsWith(".zip")) {
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
	    GawdScapeLauncher.launcher.launchGame(minecraft, gawdscape);
	    GawdScapeLauncher.launcherFrame.dispose();
            GawdScapeLauncher.launcherFrame = null;
	} catch (IOException ex) {
	    Log.error("Error loading Minecraft", ex);
	}
        long endTime = System.currentTimeMillis();
        Log.finest("Launched Minecraft in " + (endTime-startTime) + "ms");
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
