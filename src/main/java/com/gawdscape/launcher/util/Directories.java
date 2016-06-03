package com.gawdscape.launcher.util;

import java.io.File;

/**
 *
 * @author Vinnie
 */
public class Directories {

    private static File workDir = null;

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
        switch (os) {
            case LINUX:
                workingDirectory = new File(userHome, '.' + applicationName + '/');
                break;
            case WINDOWS:
                String applicationData = System.getenv("APPDATA");
                String folder = applicationData != null ? applicationData : userHome;
                workingDirectory = new File(folder, '.' + applicationName + '/');
                break;
            case OSX:
                workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
                break;
        }
	if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs())) {
	    throw new RuntimeException("The working directory could not be created: " + workingDirectory);
	}
	return workingDirectory;
    }

    public static String getAssetPath() {
	return getWorkingDirectory() + File.separator + "assets" + File.separator;
    }

    public static String getAssetIndexPath() {
	return getAssetPath() + "indexes" + File.separator;
    }

    public static String getAssetObjectPath() {
	return getAssetPath() + "objects" + File.separator;
    }

    public static String getPackDataPath() {
	return getWorkingDirectory() + File.separator + "packdata" + File.separator;
    }

    public static String getLogoPath(String pack) {
	return getPackDataPath() + "logos" + File.separator + pack + ".png";
    }

    public static String getBinPath() {
	return getWorkingDirectory() + File.separator + "bin" + File.separator;
    }

    public static String getTexperiencePath() {
	return getBinPath() + "texperience" + File.separator;
    }

    public static String getTexperienceJar(String version, String mcVersion) {
	return getTexperiencePath() + version + "_" + mcVersion + ".jar";
    }

    public static String getMcPath(String version) {
	return getBinPath() + version + File.separator;
    }

    public static String getMcJar(String version) {
	return getMcPath(version) + version + ".jar";
    }

    public static String getMcJson(String version) {
	return getMcPath(version) + version + ".json";
    }

    public static String getNativesPath(String version) {
	return getMcPath(version) + "natives" + File.separator;
    }

    public static String getLibraryPath() {
	return getWorkingDirectory() + File.separator + "libraries" + File.separator;
    }

    public static void createGameDirs(File gameDir) {
	gameDir.mkdirs();
	new File(gameDir, "resourcepacks").mkdir();
	new File(gameDir, "saves").mkdir();
	new File(gameDir, "screenshots").mkdir();
    }
}
