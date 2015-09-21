package com.gawdscape.launcher.updater;

import com.gawdscape.json.game.AssetIndex;
import com.gawdscape.json.game.Library;
import com.gawdscape.json.game.Mod;
import com.gawdscape.json.modpacks.ModType;
import com.gawdscape.launcher.DownloadDialog;
import com.gawdscape.launcher.GawdScapeLauncher;
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

    private static final int processorCores = OperatingSystem.getProcessorCores();
    public static final ExecutorService pool = Executors.newFixedThreadPool(processorCores * 2);
    public static int poolSize = 0;
    public static int thisFile = 0;
    public static String mcVer;

    public static DownloadDialog downloadDialog;

    public static void createDialog(final String name) {
	GawdScapeLauncher.logger.log(Level.INFO, "Downloading with {0} threads...", processorCores * 2);
	java.awt.EventQueue.invokeLater(() -> {
	    downloadDialog = new DownloadDialog(GawdScapeLauncher.launcherFrame);
	    downloadDialog.changeTitle("Downloading " + name + "...");
	    downloadDialog.setVisible(true);
	});
    }

    private static void addToQueue(String url, String toPath) {
	poolSize++;
	pool.submit(new DownloadTask(url, toPath));
    }

    private static void addMinecraftToQueue(String url, String toPath) {
	poolSize++;
	pool.submit(new DownloadMinecraftTask(url, toPath, mcVer));
    }

    private static void addNativeToQueue(String url, String toPath) {
	poolSize++;
	pool.submit(new DownloadNativeTask(url, toPath, mcVer));
    }

    public static boolean completeQueue() {
	pool.shutdown();
	try {
	    pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
	    return true;
	} catch (InterruptedException ex) {
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Download queue interrupted", ex);
	    return false;
	}
    }

    public static void queueAssets(AssetIndex index) {
	index.getUniqueObjects().stream().map((object)
		-> object.getHash().substring(0, 2) + "/" + object.getHash())
		.forEach((filename) -> addToQueue(
				Constants.MC_ASSET_URL + filename,
				Directories.getAssetObjectPath() + filename
			));
    }

    public static void queueMinecraft() {
	addMinecraftToQueue(
		Constants.getMcJar(mcVer),
		Directories.getMcJar(mcVer)
	);
    }

    public static void queueGawdMod(String mcVer, String gmVer) {
	addToQueue(
		Constants.getGawdModJar(gmVer, mcVer),
		Directories.getGawdModJar(gmVer, mcVer)
	);
    }

    public static void queueLibraries(Collection<Library> libraries) {
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

    private static String getModsDir(ModType type) {
	switch (type) {
	    case COREMOD:
		return "coremods";
	    case JARMOD:
		return "bin" + File.separator + "mods";
	    default:
		return "mods";
	}
    }

    public static void queueMods(String packName, Collection<Mod> mods) {
	if (mods == null) {
	    return;
	}
	mods.stream().forEach((mod) -> addToQueue(
		mod.getDownloadUrl() + mod.getArtifactPath(),
		GawdScapeLauncher.config.getGameDir(packName)
		+ File.separator + getModsDir(mod.getType())
		+ File.separator + mod.getArtifactFilename(null)
	));
    }
}
