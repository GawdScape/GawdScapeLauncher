package com.gawdscape.launcher.updater;

import com.gawdscape.launcher.updater.tasks.DownloadTask;
import com.gawdscape.launcher.updater.tasks.DownloadNativeTask;
import com.gawdscape.launcher.updater.tasks.DownloadMinecraftTask;
import com.gawdscape.json.game.AssetIndex;
import com.gawdscape.json.game.ZipArchive;
import com.gawdscape.json.game.Library;
import com.gawdscape.json.game.Mod;
import com.gawdscape.launcher.ui.DownloadDialog;
import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.launcher.updater.tasks.DownloadExtractTask;
import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.OperatingSystem;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 *
 * @author Vinnie
 */
public class DownloadManager {

    private final ExecutorService pool;
    private DownloadDialog downloadDialog;
    private String mcVer;
    private int poolSize = 0;
    private int thisFile = 0;

    public DownloadManager() {
        int processorCores = OperatingSystem.getProcessorCores();
        pool = Executors.newFixedThreadPool(processorCores * 2);
        GawdScapeLauncher.LOGGER.log(Level.INFO, "Downloading with {0} threads...", processorCores * 2);
    }

    public void createDialog(final String name) {
	java.awt.EventQueue.invokeLater(() -> {
	    downloadDialog = new DownloadDialog(GawdScapeLauncher.launcherFrame);
	    downloadDialog.changeTitle("Downloading " + name + "...");
            downloadDialog.setLocationRelativeTo(GawdScapeLauncher.launcherFrame);
	    downloadDialog.setVisible(true);
	});
    }

    public void closeDialog() {
	java.awt.EventQueue.invokeLater(() -> {
	    downloadDialog.dispose();
	    downloadDialog = null;
	});
    }

    public void setDialogLaunching() {
	java.awt.EventQueue.invokeLater(() -> {
	    downloadDialog.setLaunching();
	});
    }

    public void updateProgress() {
        java.awt.EventQueue.invokeLater(() -> {
            downloadDialog.setTotalProgress(thisFile, poolSize);
        });
    }

    public void setFile(String fileName, String host, String localPath) {
        java.awt.EventQueue.invokeLater(() -> {
            downloadDialog.setFile(fileName, host, localPath);
        });
    }

    public void setProgress(int percent, int dlKB, int totalKB) {
        java.awt.EventQueue.invokeLater(() -> {
            downloadDialog.setProgress(percent, dlKB, totalKB);
        });
    }

    public void incrementFile() {
        thisFile++;
    }

    public void setMinecraftVersion(String version) {
        this.mcVer = version;
    }

    private void addToQueue(String url, String toPath) {
	poolSize++;
	pool.submit(new DownloadTask(url, toPath));
    }

    private void addMinecraftToQueue(String url, String toPath) {
	poolSize++;
	pool.submit(new DownloadMinecraftTask(url, toPath, mcVer));
    }

    private void addNativeToQueue(String url, String toPath) {
	poolSize++;
	pool.submit(new DownloadNativeTask(url, toPath, mcVer));
    }

    private void addArchiveToQueue(String url, String toPath) {
	poolSize++;
	pool.submit(new DownloadExtractTask(url, toPath));
    }

    public boolean completeQueue() {
	pool.shutdown();
	try {
	    pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
	    return true;
	} catch (InterruptedException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Download queue interrupted", ex);
	    return false;
	}
    }

    public void queueAssets(AssetIndex index) {
	index.getUniqueObjects().stream().map((object)
		-> object.getHash().substring(0, 2) + "/" + object.getHash())
		.forEach((filename) -> addToQueue(
				Constants.MC_ASSET_URL + filename,
				Directories.getAssetObjectPath() + filename
			));
    }

    public void queueMinecraft() {
	addMinecraftToQueue(
		Constants.getMcJar(mcVer),
		Directories.getMcJar(mcVer)
	);
    }

    public void queueTexperienceMod(String mcVersion, String version) {
	addToQueue(
		Constants.getTexperienceJar(version, mcVersion),
		Directories.getTexperienceJar(version, mcVersion)
	);
    }

    public void queueLibraries(Collection<Library> libraries) {
	if (libraries == null) {
	    return;
	}
	libraries.stream().forEach((library) -> {
	    if (library.getNatives() != null) {
		String natives = library.getNatives().get(OperatingSystem.getCurrentPlatform());
		if (natives != null) {
		    String path = library.getArtifactPath(natives);
		    if (path.contains("${arch}")) {
			path = path.replace("${arch}", OperatingSystem.getArchDataModel());
		    }
		    addNativeToQueue(
			    library.getDownloadUrl() + path,
			    Directories.getLibraryPath() + path
		    );
		}
	    } else {
		addToQueue(
			library.getDownloadUrl() + library.getArtifactPath(),
			Directories.getLibraryPath() + library.getArtifactPath()
		);
	    }
	});
    }

    public void queueMods(String packName, Collection<Mod> mods) {
	if (mods == null) {
	    return;
	}
	mods.stream().forEach((mod) -> addToQueue(
		mod.getDownloadUrl() + mod.getArtifactPath(),
		GawdScapeLauncher.config.getGameDir(packName)
		+ File.separator + mod.getModsDir()
		+ File.separator + mod.getArtifactFilename(null)
	));
    }

    public void queueArchives(String packName, Collection<ZipArchive> archives) {
	if (archives == null) {
	    return;
	}
	archives.stream().forEach((archive) -> addArchiveToQueue(
		archive.getDownloadUrl() + archive.getArtifactPath(),
		GawdScapeLauncher.config.getGameDir(packName)
                + File.separator + archive.getArtifactFilename(null)
	));
    }
}
