package com.gawdscape.launcher;

import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.JsonUtils;
import com.gawdscape.launcher.util.OperatingSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

/**
 *
 * @author Vinnie
 */
public class Config {

    private static final File CONFIG_FILE = new File(Directories.getWorkingDirectory(), "config.json");
    public static boolean forceUpdate;

    private String gameDir;
    private int memory;
    private boolean showLog;
    private boolean showNews;
    private boolean closeLog;
    private boolean skipLauncher;
    private boolean joinServer;
    private boolean windowSize;
    private boolean fullscreen;
    private boolean colorLog;
    private boolean linkLog;
    private boolean globalResourcePacks;
    private String serverIP;
    private String windowWidth;
    private String windowHeight;
    private boolean customJava;
    private String javaPath;
    private String javaArgs;
    private int defaultPack;

    public Config() {
	gameDir = Directories.getWorkingDirectory().toString();
	memory = 1024;
	showLog = true;
	showNews = true;
	closeLog = false;
	skipLauncher = false;
	joinServer = false;
	windowSize = false;
	fullscreen = false;
	colorLog = true;
	linkLog = true;
	globalResourcePacks = false;
	serverIP = Constants.GS_SERVER_IP;
	windowWidth = "854";
	windowHeight = "480";
	customJava = false;
	javaPath = OperatingSystem.getJavaDir();
	javaArgs = "";
	defaultPack = 0;
    }

    public Config(
	    String gameDir,
	    int memory,
	    boolean showLog,
	    boolean showNews,
	    boolean closeLog,
	    boolean skipLauncher,
	    boolean joinServer,
	    boolean windowSize,
	    boolean fullscreen,
	    boolean colorLog,
	    boolean linkLog,
	    boolean globalResourcePacks,
	    String serverIP,
	    String windowWidth,
	    String windowHeight,
	    boolean customJava,
	    String javaPath,
	    String javaArgs,
	    int defaultPack
    ) {
	this.gameDir = gameDir;
	this.memory = memory;
	this.showLog = showLog;
	this.showNews = showNews;
	this.closeLog = closeLog;
	this.skipLauncher = skipLauncher;
	this.joinServer = joinServer;
	this.windowSize = windowSize;
	this.fullscreen = fullscreen;
	this.colorLog = colorLog;
	this.linkLog = linkLog;
	this.globalResourcePacks = globalResourcePacks;
	this.serverIP = serverIP;
	this.windowWidth = windowWidth;
	this.windowHeight = windowHeight;
	this.customJava = customJava;
	this.javaPath = javaPath;
	this.javaArgs = javaArgs;
	this.defaultPack = defaultPack;
    }

    public void setConfig(
	    String gameDir,
	    int memory,
	    boolean showLog,
	    boolean showNews,
	    boolean closeLog,
	    boolean skipLauncher,
	    boolean joinServer,
	    boolean windowSize,
	    boolean fullscreen,
	    boolean colorLog,
	    boolean linkLog,
	    boolean globalResourcePacks,
	    String serverIP,
	    String windowWidth,
	    String windowHeight
    ) {
	this.gameDir = gameDir;
	this.memory = memory;
	this.showLog = showLog;
	this.showNews = showNews;
	this.closeLog = closeLog;
	this.skipLauncher = skipLauncher;
	this.joinServer = joinServer;
	this.windowSize = windowSize;
	this.fullscreen = fullscreen;
	this.colorLog = colorLog;
	this.linkLog = linkLog;
	this.globalResourcePacks = globalResourcePacks;
	this.serverIP = serverIP;
	this.windowWidth = windowWidth;
	this.windowHeight = windowHeight;
    }

    public void setJava(
	    boolean customJava,
	    String javaPath,
	    String javaArgs
    ) {
	this.customJava = customJava;
	this.javaPath = javaPath;
	this.javaArgs = javaArgs;
    }

    public void setDefaultPack(int defaultPack) {
	this.defaultPack = defaultPack;
    }

    public int getDefaultPack() {
	return defaultPack;
    }

    public String getGameDir() {
	return gameDir;
    }

    public File getGameDirectory() {
	return new File(gameDir);
    }

    public File getGameDir(String pack) {
	return new File(gameDir, pack);
    }

    public int getMemory() {
	return memory;
    }

    public boolean getShowLog() {
	return showLog;
    }

    public boolean getShowNews() {
	return showNews;
    }

    public boolean getCloseLog() {
	return closeLog;
    }

    public boolean getSkipLauncher() {
	return skipLauncher;
    }

    public boolean getJoinServer() {
	return joinServer;
    }

    public boolean getWindowSize() {
	return windowSize;
    }

    public boolean getFullscreen() {
	return fullscreen;
    }

    public boolean getColorLog() {
	return colorLog;
    }

    public boolean getLinkLog() {
	return linkLog;
    }

    public boolean getGlobalResourcePacks() {
	return globalResourcePacks;
    }

    public String getServerIP() {
	return serverIP;
    }

    public String getWindowWidth() {
	return windowWidth;
    }

    public String getWindowHeight() {
	return windowHeight;
    }

    public boolean getCustomJava() {
	return customJava;
    }

    public String getJavaPath() {
	return javaPath;
    }

    public String getJavaArgs() {
	return javaArgs;
    }

    public static Config loadConfig() {
	String configJson = "";
	try {
	    configJson = JsonUtils.readJsonFromFile(CONFIG_FILE);
	} catch (FileNotFoundException ex) {
	    GawdScapeLauncher.LOGGER.warning("Missing config file.");
	} catch (IOException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Error loading launcher configuration.", ex);
	}
	return JsonUtils.getGson().fromJson(configJson, Config.class);
    }

    public static void saveConfig(Config config) {
	String configJson = JsonUtils.getGson().toJson(config);
	try {
	    JsonUtils.writeJsonToFile(configJson, CONFIG_FILE);
	} catch (IOException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Error saving launcher configuration.", ex);
	}
    }
}
