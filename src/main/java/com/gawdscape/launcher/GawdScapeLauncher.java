package com.gawdscape.launcher;

import com.gawdscape.launcher.ui.LauncherFrame;
import com.gawdscape.launcher.ui.LogFrame;
import com.gawdscape.launcher.ui.LoginDialog;
import com.gawdscape.json.auth.SessionResponse;
import com.gawdscape.launcher.auth.AuthManager;
import com.gawdscape.launcher.auth.SessionManager;
import com.gawdscape.launcher.launch.MinecraftLauncher;
import com.gawdscape.launcher.updater.Updater;
import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.JsonUtils;
import com.gawdscape.launcher.util.LogFormatter;
import com.gawdscape.launcher.util.LogHandler;
import com.gawdscape.launcher.util.OperatingSystem;
import com.gawdscape.launcher.util.Utils;

import java.awt.Font;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

/**
 *
 * @author Vinnie
 */
public class GawdScapeLauncher {

    public static final Logger LOGGER = Logger.getLogger("Launcher");
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
	GawdScapeLauncher.LOGGER.setUseParentHandlers(false);
	ConsoleHandler ch = new ConsoleHandler();
	ch.setFormatter(new LogFormatter());
	GawdScapeLauncher.LOGGER.addHandler(ch);
	GawdScapeLauncher.LOGGER.setLevel(Level.ALL);
    }

    public static void setUIFont (FontUIResource font){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof FontUIResource)
                UIManager.put(key, font);
        }
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
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
	    LOGGER.log(Level.SEVERE, "Error setting look and feel.", ex);
	}

        setUIFont(new FontUIResource("Arial", Font.PLAIN, 12));
	//</editor-fold>

	// Load config
	long startTime2 = System.currentTimeMillis();
	LOGGER.info("Loading config...");
	config = Config.loadConfig();
	if (config == null) {
	    config = new Config();
	}
	Utils.benchmark("Config took {0}ms", startTime2);

	/* Create and display the form */
	if (config.getShowLog()) {
	    startTime2 = System.currentTimeMillis();
	    LOGGER.info("Initializing Log.");
	    logFrame = new LogFrame(config.getColorLog(), config.getLinkLog());
	    if (config.getShowLog()) {
		LOGGER.addHandler(new LogHandler());
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

	/*if (offlineMode) {
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
	}*/

	try {
	    modpacks.downloadPacks();
	} catch (IOException ex) {
	    LOGGER.log(Level.SEVERE, "Error downloading mod pack index.", ex);
	}

	try {
	    modpacks.loadCustomPacks();
	} catch (IOException ex) {
	    LOGGER.log(Level.SEVERE, "Error loading custom mod pack index.", ex);
	}
	Utils.benchmark("Loaded mod packs in {0}ms", startTime2);

	SessionManager sessionManager = SessionManager.loadSessions();

	// Load and refresh last saved session
	LOGGER.info("Checking for saved session...");
	if (sessionManager.shouldAutoLogin()) {
	    startTime2 = System.currentTimeMillis();
	    LOGGER.log(Level.INFO, "Session found. Refreshing session for {0}", sessionManager.getAutoLoginUser());
	    session = AuthManager.refresh(sessionManager.getAutoLoginToken());
	    Utils.benchmark("Auto Login took {0}ms", startTime2);
	}

	// Are we logged in?
	if (session != null && session.getAccessToken() != null) {
	    sessionManager.addSession(session);
	    SessionManager.saveSessions(sessionManager);
	    // Valid session, continue
	    LOGGER.log(Level.INFO, "Refreshed session for {0}", session.getSelectedProfile().getName());
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
		LOGGER.severe("Error refreshing session:");
		LOGGER.severe(session.getErrorMessage());
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
	LOGGER.info("Checking for launcher update...");
	try {
	    int version = Integer.parseInt(
		    JsonUtils.readJsonFromUrl(Constants.LAUNCHER_VERSION_URL));
	    if (version > Constants.VERSION) {
		LOGGER.log(Level.WARNING, "Launcher version {0} has been released.", version);
		promptUpdate(version);
	    } else {
		LOGGER.info("No launcher update found.");
	    }
	} catch (IOException ex) {
	    LOGGER.log(Level.SEVERE, "Error checking for launcher update.", ex);
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
