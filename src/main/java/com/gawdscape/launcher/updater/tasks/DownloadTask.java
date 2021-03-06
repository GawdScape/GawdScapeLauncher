package com.gawdscape.launcher.updater.tasks;

import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.launcher.updater.ProgressDelegate;
import com.gawdscape.launcher.updater.RBCWrapper;
import com.gawdscape.launcher.util.FileUtils;

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
import java.util.logging.Level;

/**
 *
 * @author Vinnie
 */
public class DownloadTask implements Callable, ProgressDelegate {

    final File file;
    URL url;
    private int tries;

    public DownloadTask(String remoteURL, String localPath) {
	file = new File(localPath);
	file.getParentFile().mkdirs();
	try {
	    url = new URL(remoteURL);
	} catch (MalformedURLException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Bad download URL for file: " + file.getName(), ex);
	}
    }

    private String getCurrentMD5() {
	try {
	    return FileUtils.checkSum(new FileInputStream(file));
	    //GawdScapeLauncher.logger.log(Level.FINE, "{0} [Local: {1}, Remote: {2}]", new Object[]{file.getName(), currentMD5, expectedMD5});
	} catch (FileNotFoundException ex) {
	    return "-1";
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
	} catch (IOException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "[" + (tries + 1) + "/3] Error downloading file: " + file.getName(), ex);
	}
    }

    public boolean attemptDownload() {
	HttpURLConnection connection = createConnection(url);

	String expectedMD5 = connection.getHeaderField("ETag");
        switch (expectedMD5.length()) {
            case 34:
                expectedMD5 = expectedMD5.substring(1, 33);
                break;
            case 32:
                break;
            default:
                expectedMD5 = "0";
                break;
        }

	if (expectedMD5.equals(getCurrentMD5())) {
	    GawdScapeLauncher.LOGGER.log(Level.FINE, "[{0}/3] Skipping: {1}", new Object[]{tries + 1, file.getName()});
	    return true;
	}

	//GawdScapeLauncher.logger.log(Level.FINE, "Saving to: {0}", file.toString());
	downloadFile(connection);

	if (expectedMD5.equals("0")) {
	    return true;
	}

	while (tries < 3) {
	    if (expectedMD5.equals(getCurrentMD5())) {
		return true;
	    }
	    GawdScapeLauncher.LOGGER.log(Level.WARNING, "[{0}/3] {1} File hash did not match.", new Object[]{tries + 1, file.getName()});
	    tries++;
	    if (attemptDownload()) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public void progressCallback(RBCWrapper rbc, double progress) {
	GawdScapeLauncher.updater.getDownloadManager().setFile(file.getName(), url.getHost(), file.getParentFile().getPath());
	GawdScapeLauncher.updater.getDownloadManager().setProgress((int) progress, (int) rbc.getReadSoFar(), (int) rbc.getExpectedSize());
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
		    GawdScapeLauncher.LOGGER.log(Level.FINE, "URL: {0}\n\t->Redirect: {1}", new Object[]{url.toString(), location});
		    connection = (HttpURLConnection) new URL(location).openConnection();
		}
	    }
	} catch (IOException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "[" + (tries + 1) + "/3] Error connecting to URL " + url.toString(), ex);
	}

	return connection;
    }

    @Override
    public Object call() throws Exception {
	GawdScapeLauncher.LOGGER.log(Level.INFO, "[1/3] Starting download of: {0}", file.getName());
	GawdScapeLauncher.updater.getDownloadManager().incrementFile();
	attemptDownload();
	GawdScapeLauncher.updater.getDownloadManager().updateProgress();
	return true;
    }
}
