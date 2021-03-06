package com.gawdscape.launcher.util;

import com.gawdscape.launcher.GawdScapeLauncher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

/**
 *
 * @author Vinnie
 */
public class FileUtils {

    public static void copyFile(File sourceFile, File destFile) throws IOException {
	if (!destFile.exists()) {
	    destFile.getParentFile().mkdirs();
	    destFile.createNewFile();
	}

	FileChannel source = null;
	FileChannel destination = null;

	try {
	    source = new FileInputStream(sourceFile).getChannel();
	    destination = new FileOutputStream(destFile).getChannel();
	    destination.transferFrom(source, 0, source.size());
	} finally {
	    if (source != null) {
		source.close();
	    }
	    if (destination != null) {
		destination.close();
	    }
	}
    }

    public static void delete(File f) throws IOException {
	if (f.isDirectory()) {
	    for (File c : f.listFiles()) {
		delete(c);
	    }
	}
	if (!f.delete()) {
	    throw new FileNotFoundException("Failed to delete file: " + f);
	}
    }

    public static void downloadFile(String url, File destination) {
	try {
	    URL website = new URL(url);
	    try (ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		    FileOutputStream fos = new FileOutputStream(destination)) {
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	    }
	} catch (FileNotFoundException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Error: File Not Found");
	} catch (MalformedURLException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Error: Invalid URL");
	} catch (IOException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "IO Error", ex);
	}
    }

    public static boolean existsOnInternet(String url){
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (IOException ex) {
            GawdScapeLauncher.LOGGER.log(Level.SEVERE, "IO Error", ex);
            return false;
        }
      }

    public static String checkSum(FileInputStream fis) {
	String checksum = null;
	try {
	    MessageDigest md = MessageDigest.getInstance("MD5");

	    //Using MessageDigest update() method to provide input
	    byte[] buffer = new byte[8192];
	    int numOfBytesRead;
	    while ((numOfBytesRead = fis.read(buffer)) > 0) {
		md.update(buffer, 0, numOfBytesRead);
	    }
	    byte[] hash = md.digest();
	    checksum = new BigInteger(1, hash).toString(16); //don't use this, truncates leading zero
	    fis.close();
	} catch (IOException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Error hashing file", ex);
	} catch (NoSuchAlgorithmException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Error calculating MD5", ex);
	}

	return checksum;
    }
}
