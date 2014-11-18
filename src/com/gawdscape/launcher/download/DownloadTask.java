package com.gawdscape.launcher.download;

import com.gawdscape.launcher.util.FileUtils;
import com.gawdscape.launcher.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.Callable;

/**
 *
 * @author Vinnie
 */
public class DownloadTask implements Callable, RBCWrapperDelegate {

    URL url;
    File file;
    double progress;
    String expectedMD5;
    String currentMD5;

    public DownloadTask(String remoteURL, String localPath) {
	try {
	    url = new URL(remoteURL);
	} catch (MalformedURLException ex) {
	    Log.error("Bad download URL", ex);
	}
	file = new File(localPath);
	file.getParentFile().mkdirs();
    }

    public void downloadFile(URL url, File file) {
	HttpURLConnection connection = createConnection(url);

	expectedMD5 = connection.getHeaderField("ETag");
	if (expectedMD5.length() != 34) {
	    expectedMD5 = "0";
	} else {
	    expectedMD5 = expectedMD5.substring(1, 33);
	}
	Log.finest("Expected MD5: " + expectedMD5 + " - " + file.getName());

	currentMD5 = "-1";
	if (file.exists()) {
	    try {
		currentMD5 = FileUtils.checkSum(new FileInputStream(file));
		Log.finest("Current MD5: " + currentMD5 + " - " + file.getName());
	    } catch (FileNotFoundException ex) {
		currentMD5 = "-2";
	    }
	}

	if (currentMD5.equals(expectedMD5)) {
	    Log.finer("Skipping " + file.getName());
	} else {
	    Log.finer("Saving to: " + file.toString());
	    FileOutputStream fos;
	    ReadableByteChannel rbc;
	    try {
		rbc = new RBCWrapper(Channels.newChannel(connection.getInputStream()), connection.getContentLength(), this);
		fos = new FileOutputStream(file);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
	    } catch (IOException e) {
		Log.error("Error downloading file", e);
	    }
	}
    }

    @Override
    public void rbcProgressCallback(RBCWrapper rbc, double progress) {
	this.progress = progress;
	DownloadManager.downloadDialog.setFile(file.getName(), url.getHost(), file.getParentFile().getPath());
	DownloadManager.downloadDialog.setProgress((int) progress, (int) rbc.getReadSoFar(), (int) rbc.getExpectedSize());
    }

    private HttpURLConnection createConnection(URL url) {
	HttpURLConnection connection = null;
	try {
	    HttpURLConnection.setFollowRedirects(false);

	    connection = (HttpURLConnection) url.openConnection();

	    int status = connection.getResponseCode();
	    if (status != HttpURLConnection.HTTP_OK) {
		if (status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_SEE_OTHER) {
		    String location = connection.getHeaderField("Location");
		    Log.fine("Being redirected to: " + location);
		    connection = (HttpURLConnection) new URL(location).openConnection();
		}
	    }
	} catch (IOException e) {
	}

	return connection;
    }

    @Override
    public Object call() throws Exception {
	Log.finer("Strating download of: " + url.toString());
	DownloadManager.thisFile++;
	downloadFile(url, file);
	DownloadManager.downloadDialog.setTotalProgress(DownloadManager.thisFile, DownloadManager.poolSize);
	return true;
    }
}
