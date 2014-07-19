package com.gawdscape.launcher.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
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
	} catch (IOException ex) {
	    Log.error("Error calculating MD5", ex);
	} catch (NoSuchAlgorithmException ex) {
	    Log.error("Error calculating MD5", ex);
	}

	return checksum;
    }
}
