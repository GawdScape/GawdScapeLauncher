package com.gawdscape.launcher.updater.tasks;

import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.launcher.updater.DownloadManager;
import com.gawdscape.launcher.util.FileUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author Vinnie
 */
public class DownloadExtractTask extends DownloadTask {

    public DownloadExtractTask(String remoteURL, String localPath) {
	super(remoteURL, localPath);
    }

    private void unZip() throws IOException {
	GawdScapeLauncher.logger.log(Level.INFO, "Starting to extract {0}", file.getName());
	ZipFile zip = new ZipFile(file);
	Enumeration entries = zip.entries();
	while (entries.hasMoreElements()) {
	    ZipEntry entry = (ZipEntry) entries.nextElement();
	    File target = new File(file.getParentFile(), entry.getName());
	    DownloadManager.downloadDialog.setFile(target.getName(), file.getName(), target.getParent());

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
	GawdScapeLauncher.logger.log(Level.FINE, "Starting download of archive: {0}", file.getName());
	DownloadManager.thisFile++;
	if (attemptDownload()) {
	    try {
		unZip();
	    } catch (IOException ex) {
		GawdScapeLauncher.logger.log(Level.SEVERE, "Error extracting " + file.getName(), ex);
	    }
	}
        FileUtils.delete(file);
	DownloadManager.downloadDialog.setTotalProgress(DownloadManager.thisFile, DownloadManager.poolSize);
	return true;
    }
}
