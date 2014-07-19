package com.gawdscape.launcher.download;

import com.gawdscape.launcher.DownloadDialog;
import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.launcher.game.AssetIndex;
import com.gawdscape.launcher.game.Library;
import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.FileUtils;
import com.gawdscape.launcher.util.Log;
import com.gawdscape.launcher.util.OperatingSystem;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author Vinnie
 */
public class DownloadManager {

    public static final int processorCores = Runtime.getRuntime().availableProcessors();
    public static ExecutorService pool = Executors.newFixedThreadPool(processorCores);
    public static int poolSize = 0;
    public static int thisFile = 0;

    public static DownloadDialog downloadDialog;

    public static void createDialog() {
	Log.info("Downloading with " + processorCores + " threads...");
	java.awt.EventQueue.invokeLater(new Runnable() {
	    public void run() {
		downloadDialog = new DownloadDialog(GawdScapeLauncher.launcherFrame, true);
		downloadDialog.setTitle("Downloading GawdScape...");
		downloadDialog.setVisible(true);
	    }
	});
    }

    public static void setFile(String fileName, String host, String localPath) {
	downloadDialog.setFile(fileName, host, localPath);
    }

    public static void setProgress(int percent, int downloaded, int total) {
	downloadDialog.setProgress(percent, downloaded, total);
    }

    public static void setTotalProgress(int current, int total) {
	downloadDialog.setTotalProgress(current, total);
    }

    public static void addToQueue(String url, String toPath) {
	poolSize++;
	pool.submit(new DownloadTask(url, toPath));
    }

    public static boolean completeQueue() {
	pool.shutdown();
	try {
	    pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
	    return true;
	} catch (InterruptedException ex) {
	    Log.error("Download queue interrupted", ex);
	    return false;
	}
    }

    public static void queueAssets(AssetIndex index) {
	for (AssetIndex.AssetObject object : index.getUniqueObjects()) {
	    String filename = object.getHash().substring(0, 2) + "/" + object.getHash();
	    addToQueue(
		    Constants.MC_ASSET_URL + filename,
		    Directories.getAssetObjectPath() + filename
	    );
	}
    }

    public static void queueMinecraft(String mcVer, String gsVer) {
	addToQueue(
		Constants.MC_DOWNLOAD_URL + "versions/" + mcVer + "/" + mcVer + ".jar",
		Directories.getBinPath() + "minecraft.jar"
	);
	addToQueue(
		Constants.GS_DOWNLOAD_URL + "gawdscape/" + gsVer + "/" + gsVer + ".jar",
		Directories.getBinPath() + "gawdscape.jar"
	);
    }

    public static void queueLibraries(Collection<Library> mcLibraries, Collection<Library> gsLibraries) {
	if (mcLibraries != null) {
	    for (Library library : mcLibraries) {
		if (library.getNatives() != null) {
		    String natives = (String) library.getNatives().get(OperatingSystem.getCurrentPlatform());
		    if (natives != null) {
			String path = library.getArtifactPath(natives);
			if (path.contains("${arch}")) {
			    path = path.replace("${arch}", OperatingSystem.getArchDataModel());
			}
			addToQueue(
				Constants.MC_LIBRARY_URL + path,
				Directories.getLibraryPath() + path
			);
		    }
		} else {
		    addToQueue(
			    Constants.MC_LIBRARY_URL + library.getArtifactPath(),
			    Directories.getLibraryPath() + library.getArtifactPath()
		    );
		}
	    }
	}
	if (gsLibraries != null) {
	    for (Library library : gsLibraries) {
		if (library.getNatives() != null) {
		    String natives = (String) library.getNatives().get(OperatingSystem.getCurrentPlatform());
		    if (natives != null) {
			String path = library.getArtifactPath(natives);
			if (path.contains("${arch}")) {
			    path = path.replace("${arch}", OperatingSystem.getArchDataModel());
			}
			addToQueue(
				Constants.GS_LIBRARY_URL + path,
				Directories.getLibraryPath() + path
			);
		    }
		} else {
		    addToQueue(
			    Constants.GS_LIBRARY_URL + library.getArtifactPath(),
			    Directories.getLibraryPath() + library.getArtifactPath()
		    );
		}
	    }
	}
    }

    public static void extractNatives(Collection<Library> mcLibraries) {
	File nativesDir = new File(Directories.getNativesPath());
	if (nativesDir.exists()) {
	    try {
		FileUtils.delete(nativesDir);
	    } catch (IOException ex) {
		Log.error("Error deleting old natives directory", ex);
	    }
	}
	downloadDialog.setExtracting();
	if (mcLibraries != null) {
	    for (Library library : mcLibraries) {
		if (library.getNatives() != null) {
		    String natives = (String) library.getNatives().get(OperatingSystem.getCurrentPlatform());
		    if (natives != null) {
			String path = library.getArtifactPath(natives);
			if (path.contains("${arch}")) {
			    path = path.replace("${arch}", OperatingSystem.getArchDataModel());
			}
			try {
			    unZipNazives(path);
			} catch (IOException ex) {
			    Log.error("Error extracting " + path, ex);
			}
		    }
		}
	    }
	}
    }

    public static void unZipNazives(String path) throws IOException {
	File archive = new File(Directories.getLibraryPath(), path);
	Log.info("Starting to extract " + archive.toString());
	ZipFile zip = new JarFile(archive);
	Enumeration entries = zip.entries();
	while (entries.hasMoreElements()) {
	    ZipEntry entry = (ZipEntry) entries.nextElement();
	    File target = new File(Directories.getNativesPath(), entry.getName());
	    downloadDialog.setFile(target.getName(), archive.getName(), target.getParent());

	    if (entry.getName().contains("META-INF")) {
		continue;
	    }

	    target.getParentFile().mkdirs();

	    if (!entry.isDirectory()) {
		BufferedInputStream inputStream = new BufferedInputStream(zip.getInputStream(entry));

		byte[] buffer = new byte[2048];
		FileOutputStream outputStream = new FileOutputStream(target);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
		try {
		    int length;
		    while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
			bufferedOutputStream.write(buffer, 0, length);
		    }
		} finally {
		    bufferedOutputStream.close();
		    outputStream.close();
		    inputStream.close();
		}
	    }
	}
    }

    public static void removeMinecraftMetaInf() {
	Log.info("Removing META-INF from minecraft.jar...");
	downloadDialog.setTitle("Removing META-INF...");
	File inputFile = new File(Directories.getBinPath(), "minecraft.jar");
	File outputTmpFile = new File(Directories.getBinPath(), "minecraft.jar.tmp");

	downloadDialog.setFile("/META-INF", inputFile.getName(), outputTmpFile.toString());

	try {
	    JarInputStream input = new JarInputStream(new FileInputStream(inputFile));
	    JarOutputStream output = new JarOutputStream(new FileOutputStream(outputTmpFile));
	    JarEntry entry;

	    while ((entry = input.getNextJarEntry()) != null) {
		if (entry.getName().contains("META-INF")) {
		    continue;
		}
		output.putNextEntry(entry);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = input.read(buffer, 0, buffer.length)) != -1) {
		    output.write(buffer, 0, length);
		}
		output.closeEntry();
	    }

	    input.close();
	    output.close();

	    if (!inputFile.delete()) {
		Log.severe("Failed to delete minecraft.jar");
		return;
	    }
	    if (!outputTmpFile.renameTo(inputFile)) {
		Log.severe("Failed to rename minecraft.jar.tmp");
	    }
	} catch (IOException e) {
	    Log.error("Error removing META-INF", e);
	}
    }
}
