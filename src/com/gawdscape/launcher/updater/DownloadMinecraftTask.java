package com.gawdscape.launcher.updater;

import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

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

	public void removeMetaInf() {
		Log.info("Removing META-INF from Minecraft " + mcVersion + ".jar...");
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
				Log.severe("Failed to delete Minecraft " + mcVersion + ".jar");
			}
			if (!outputTmpFile.renameTo(inputFile)) {
				Log.severe("Failed to rename Minecraft " + mcVersion + ".jar.tmp");
			}
		} catch (IOException e) {
			Log.error("Error removing META-INF", e);
		}
	}

	@Override
	public Object call() throws Exception {
		Log.finer("Strating download of minecraft: " + url.toString());
		DownloadManager.thisFile++;
		if (attemptDownload()) {
			removeMetaInf();
		}
		DownloadManager.downloadDialog.setTotalProgress(DownloadManager.thisFile, DownloadManager.poolSize);
		return true;
	}
}
