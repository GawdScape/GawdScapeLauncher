package com.gawdscape.launcher.updater.tasks;

import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.launcher.util.Directories;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author Vinnie
 */
public class DownloadNativeTask extends DownloadTask {

    private final String mcVersion;

    public DownloadNativeTask(String remoteURL, String localPath, String mcVersion) {
	super(remoteURL, localPath);
	this.mcVersion = mcVersion;
    }

    private void unZip() throws IOException {
	GawdScapeLauncher.LOGGER.log(Level.INFO, "Starting to extract {0}", file.getName());
	ZipFile zip = new JarFile(file);
	Enumeration entries = zip.entries();
	while (entries.hasMoreElements()) {
	    ZipEntry entry = (ZipEntry) entries.nextElement();
	    File target = new File(Directories.getNativesPath(mcVersion), entry.getName());
	    GawdScapeLauncher.updater.getDownloadManager().setFile(target.getName(), file.getName(), target.getParent());

	    if (entry.getName().contains("META-INF")) {
		continue;
	    }

	    target.getParentFile().mkdirs();

	    if (!entry.isDirectory()) {
		BufferedInputStream inputStream = new BufferedInputStream(zip.getInputStream(entry));

		byte[] buffer = new byte[2048];
		FileOutputStream outputStream = new FileOutputStream(target);
		try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
		    int length;
		    while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
			bufferedOutputStream.write(buffer, 0, length);
		    }
		} finally {
		    outputStream.close();
		    inputStream.close();
		}
	    }
	}
    }

    @Override
    public Object call() throws Exception {
	GawdScapeLauncher.LOGGER.log(Level.FINE, "Starting download of native: {0}", file.getName());
	GawdScapeLauncher.updater.getDownloadManager().incrementFile();
	if (attemptDownload()) {
	    try {
		unZip();
	    } catch (IOException ex) {
		GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Error extracting " + file.getName(), ex);
	    }
	}
	GawdScapeLauncher.updater.getDownloadManager().updateProgress();
	return true;
    }
}
