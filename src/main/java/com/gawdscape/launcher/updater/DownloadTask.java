package com.gawdscape.launcher.updater;

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
public class DownloadTask implements Callable, ProgressDelegate {

	URL url;
	File file;
	double progress;
	String expectedMD5;
	String currentMD5;
	int tries;

	public DownloadTask(String remoteURL, String localPath) {
		try {
			url = new URL(remoteURL);
		} catch (MalformedURLException ex) {
			Log.error("Bad download URL", ex);
		}
		file = new File(localPath);
		file.getParentFile().mkdirs();
	}

	public void getCurrentMD5() {
		currentMD5 = "-1";
		if (file.exists()) {
			try {
				currentMD5 = FileUtils.checkSum(new FileInputStream(file));
				Log.fine(file.getName()
						+ " [Local: " + currentMD5 + ", Remote: " + expectedMD5 + "]");
			} catch (FileNotFoundException ex) {
				currentMD5 = "-2";
			}
		}
	}

	private void downloadFile(HttpURLConnection connection) {
		FileOutputStream fos;
		ReadableByteChannel rbc;
		try {
			rbc = new RBCWrapper(Channels.newChannel(connection.getInputStream()), connection.getContentLength(), this);
			fos = new FileOutputStream(file);
			// The actual fucking download line
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			// ^^
			fos.close();
			rbc.close();
			connection.disconnect();
		} catch (IOException e) {
			Log.error("Error downloading file", e);
		}
	}

	public boolean attemptDownload() {
		HttpURLConnection connection = createConnection(url);

		expectedMD5 = connection.getHeaderField("ETag");
		if (expectedMD5.length() == 34) {
			expectedMD5 = expectedMD5.substring(1, 33);
		} else if (expectedMD5.length() == 32) {
		} else {
			expectedMD5 = "0";
		}

		getCurrentMD5();

		if (currentMD5.equals(expectedMD5)) {
			Log.finer("Skipping " + file.getName());
			return true;
		}

		Log.finer("Saving to: " + file.toString());
		downloadFile(connection);

		if (expectedMD5.equals("0")) {
			return true;
		}

		while (tries < 3) {
			getCurrentMD5();
			if (currentMD5.equals(expectedMD5)) {
				return true;
			}
			Log.warning("[" + (tries + 1) + "/3] Download Failed " + file.getName());
			downloadFile(createConnection(url));
			tries++;
		}
		return false;
	}

	@Override
	public void progressCallback(RBCWrapper rbc, double progress) {
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
		attemptDownload();
		DownloadManager.downloadDialog.setTotalProgress(DownloadManager.thisFile, DownloadManager.poolSize);
		return true;
	}
}
