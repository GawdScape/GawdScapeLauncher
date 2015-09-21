package com.gawdscape.launcher;

import com.gawdscape.json.auth.SessionResponse;
import com.gawdscape.launcher.auth.AuthManager;
import com.gawdscape.launcher.auth.SessionManager;
import com.gawdscape.launcher.launch.MinecraftLauncher;
import com.gawdscape.launcher.updater.Updater;
import com.gawdscape.launcher.util.*;

import javax.swing.*;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vinnie
 */
public class GawdScapeLauncher {

    public static final Logger logger = Logger.getLogger("Launcher");
    public static Config config;
    public static LogFrame logFrame;
    public static LauncherFrame launcherFrame;
    public static LoginDialog loginDialog;
    public static SessionResponse session;
    public static Updater updater;
    public static MinecraftLauncher launcher;
    public static ModPackManager modpacks;
    public static boolean offlineMode;

    public static void initLogger() {
	GawdScapeLauncher.logger.setUseParentHandlers(false);
	ConsoleHandler ch = new ConsoleHandler();
	ch.setFormatter(new LogFormatter());
	GawdScapeLauncher.logger.addHandler(ch);
	GawdScapeLauncher.logger.setLevel(Level.ALL);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
	long startTime = System.currentTimeMillis();

	// Configure logger
	initLogger();

	/* Set the System look and feel */
	//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code ">
	try {
	    javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
	    logger.log(Level.SEVERE, "Error setting look and feel.", ex);
	}
	//</editor-fold>

	// Load config
	long startTime2 = System.currentTimeMillis();
	logger.info("Loading config...");
	config = Config.loadConfig();
	if (config == null) {
	    config = new Config();
	}
	Utils.benchmark("Config took {0}ms", startTime2);

	/* Create and display the form */
	if (config.getShowLog()) {
	    startTime2 = System.currentTimeMillis();
	    logger.info("Initializing Log.");
	    logFrame = new LogFrame(config.getColorLog(), config.getLinkLog());
	    if (config.getShowLog()) {
		logger.addHandler(new LogHandler());
	    }
	    logFrame.setVisible(true);
	    Utils.benchmark("Log took {0}ms", startTime2);
	}

	/*startTime2 = System.currentTimeMillis();
	 logger.info("Pinging Mojang Auth Server. Waiting 15 seconds for response.");
	 try {
	 InetAddress inet = InetAddress.getByName(Constants.MC_AUTH_IP);
	 if (!inet.isReachable(Constants.MC_AUTH_TIMEOUT)) {
	 offlineMode = true;
	 logger.warning("Auth Server offline. Enabling offline mode.");
	 }
	 } catch (IOException ex) {
	 offlineMode = true;
	 logger.warning("Error connecting to Auth Server. Enabling offline mode.");
	 }
	 Utils.benchmark("Auth Server ping took ", startTime2);*/
	// Get list of mod packs
	startTime2 = System.currentTimeMillis();
	modpacks = new ModPackManager();

	if (offlineMode) {
	    try {
		modpacks.loadLocalPacks();
		modpacks.loadCustomPacks();
		launcherFrame = new LauncherFrame(false);
		launcherFrame.setOffline();
		launcherFrame.setVisible(true);
		Utils.benchmark("Opened GawdScape Launcher in {0}ms", startTime);
	    } catch (IOException ex) {
		logger.log(Level.SEVERE, "Error loading local mod pack index.", ex);
	    }
	    return;
	}

	try {
	    modpacks.downloadPacks();
	} catch (IOException ex) {
	    logger.log(Level.SEVERE, "Error downloading mod pack index.", ex);
	}

	try {
	    modpacks.loadCustomPacks();
	} catch (IOException ex) {
	    logger.log(Level.SEVERE, "Error loading custom mod pack index.", ex);
	}
	Utils.benchmark("Loaded mod packs in {0}ms", startTime2);

	SessionManager sessionManager = SessionManager.loadSessions();

	// Load and refresh last saved session
	logger.info("Checking for saved session...");
	if (sessionManager.shouldAutoLogin()) {
	    startTime2 = System.currentTimeMillis();
	    logger.log(Level.INFO, "Session found. Refreshing session for {0}", sessionManager.getAutoLoginUser());
	    session = AuthManager.refresh(sessionManager.getAutoLoginToken());
	    Utils.benchmark("Auto Login took {0}ms", startTime2);
	}

	// Are we logged in?
	if (session != null && session.getAccessToken() != null) {
	    sessionManager.addSession(session);
	    SessionManager.saveSessions(sessionManager);
	    // Valid session, continue
	    logger.log(Level.INFO, "Refreshed session for {0}", session.getSelectedProfile().getName());
	    // Should we skip the launcher and just launch Minecraft?
	    if (config.getSkipLauncher()) {
		updater = new Updater(
			modpacks.getPackById(config.getDefaultPack()));
		updater.start();
	    } else {
		// Initialize launcher frame
		startTime2 = System.currentTimeMillis();
		launcherFrame = new LauncherFrame(config.getShowNews());
		launcherFrame.setUsername(session.getSelectedProfile().getName());
		launcherFrame.setVisible(true);
		Utils.benchmark("Loaded Launcher Frame in {0}ms", startTime2);
	    }
	} else {
	    // Nope, Login
	    startTime2 = System.currentTimeMillis();
	    launcherFrame = new LauncherFrame(config.getShowNews());
	    loginDialog = new LoginDialog(launcherFrame, sessionManager);
	    if (session != null && session.getError() != null) {
		loginDialog.setError(session.getErrorMessage());
		logger.severe("Error refreshing session:");
		logger.severe(session.getErrorMessage());
	    }
	    loginDialog.setVisible(true);
	    Utils.benchmark("Loaded Login Dialog in {0}ms", startTime2);
	}

	startTime2 = System.currentTimeMillis();
	checkLauncherUpdate();
	Utils.benchmark("Launcher update check took {0}ms", startTime2);

	Utils.benchmark("Opened GawdScape Launcher in {0}ms", startTime);
    }

    private static void checkLauncherUpdate() {
	logger.info("Checking for launcher update...");
	try {
	    int version = new Integer(
		    JsonUtils.readJsonFromUrl(Constants.LAUNCHER_VERSION_URL));
	    if (version > Constants.VERSION) {
		logger.log(Level.WARNING, "Launcher version {0} has been released.", version);
		promptUpdate(version);
	    } else {
		logger.info("No launcher update found.");
	    }
	} catch (IOException ex) {
	    logger.log(Level.SEVERE, "Error checking for launcher update.", ex);
	}
    }

    private static void promptUpdate(int version) {
	int n = JOptionPane.showConfirmDialog(
		launcherFrame,
		"Launcher version " + version + " has been released."
		+ "\nWould you like to download now?",
		"Launcher Update",
		JOptionPane.YES_NO_OPTION);
	if (n == 0) {
	    OperatingSystem.openLink(Constants.UPDATE_LINK);
	}
    }
}
