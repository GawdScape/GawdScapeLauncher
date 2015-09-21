package com.gawdscape.launcher.updater;

import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.launcher.util.Directories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.logging.Level;

/**
 *
 * @author Vinnie
 */
public class DownloadMinecraftTask extends DownloadTask {

    private final String mcVersion;

    public DownloadMinecraftTask(String remoteURL, String localPath, String mcVersion) {
	super(remoteURL, localPath);
	this.mcVersion = mcVersion;
    }

    private void removeMetaInf() {
	GawdScapeLauncher.logger.log(Level.INFO, "Removing META-INF from Minecraft {0}.jar...", mcVersion);
	//DownloadManager.downloadDialog.changeTitle("Removing META-INF...");
	File inputFile = new File(Directories.getMcJar(mcVersion));
	File outputTmpFile = new File(Directories.getMcJar(mcVersion) + ".tmp");

	DownloadManager.downloadDialog.setFile("/META-INF", inputFile.getName(), outputTmpFile.toString());

	try (
		JarInputStream input = new JarInputStream(new FileInputStream(inputFile));
		JarOutputStream output = new JarOutputStream(new FileOutputStream(outputTmpFile))) {
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
	    output.close();
	    input.close();

	    if (!inputFile.delete()) {
		GawdScapeLauncher.logger.log(Level.SEVERE, "Failed to delete Minecraft {0}.jar", mcVersion);
	    }
	    if (!outputTmpFile.renameTo(inputFile)) {
		GawdScapeLauncher.logger.log(Level.SEVERE, "Failed to rename Minecraft {0}.jar.tmp", mcVersion);
	    }
	} catch (IOException e) {
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Error removing META-INF", e);
	}
    }

    @Override
    public Object call() throws Exception {
	GawdScapeLauncher.logger.log(Level.FINE, "Starting download of minecraft: {0}", url.toString());
	DownloadManager.thisFile++;
	if (attemptDownload()) {
	    removeMetaInf();
	}
	DownloadManager.downloadDialog.setTotalProgress(DownloadManager.thisFile, DownloadManager.poolSize);
	return true;
    }
}
