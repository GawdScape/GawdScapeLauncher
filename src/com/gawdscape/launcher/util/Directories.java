package com.gawdscape.launcher.util;

import java.io.File;

/**
 *
 * @author Vinnie
 */
public class Directories {

    private static File workDir = null;
    private static final String separator = File.separator;

    public static File getWorkingDirectory() {
	if (workDir == null) {
	    workDir = getWorkingDirectory("gawdscape");
	}
	return workDir;
    }

    public static File getWorkingDirectory(String applicationName) {
	String userHome = System.getProperty("user.home", ".");
	File workingDirectory = new File(userHome, applicationName + '/');
	OperatingSystem os = OperatingSystem.getCurrentPlatform();
	if (os.equals(OperatingSystem.LINUX)) {
	    workingDirectory = new File(userHome, '.' + applicationName + '/');
	}
	if (os.equals(OperatingSystem.WINDOWS)) {
	    String applicationData = System.getenv("APPDATA");
	    String folder = applicationData != null ? applicationData : userHome;

	    workingDirectory = new File(folder, '.' + applicationName + '/');
	}
	if (os.equals(OperatingSystem.OSX)) {
	    workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
	}
	if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs())) {
	    throw new RuntimeException("The working directory could not be created: " + workingDirectory);
	}
	return workingDirectory;
    }

    public static String getAssetPath() {
	return getWorkingDirectory() + separator + "assets" + separator;
    }

    public static String getAssetIndexPath() {
	return getAssetPath() + "indexes" + separator;
    }

    public static String getAssetObjectPath() {
	return getAssetPath() + "objects" + separator;
    }

    public static String getLibraryPath() {
	return getWorkingDirectory() + separator + "libraries" + separator;
    }

    public static String getBinPath() {
	return getWorkingDirectory() + separator + "bin" + separator;
    }

    public static String getModPath() {
	return getBinPath() + "mods" + separator;
    }

    public static String getNativesPath() {
	return getBinPath() + "natives" + separator;
    }

    public static void createGameDirs(File gameDir) {
		gameDir.mkdirs();
		new File(gameDir, "resourcepacks").mkdir();
		new File(gameDir, "saves").mkdir();
		new File(gameDir, "screenshots").mkdir();
    }
}
