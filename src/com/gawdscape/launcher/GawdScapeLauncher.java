package com.gawdscape.launcher;

import com.gawdscape.launcher.auth.AuthManager;
import com.gawdscape.launcher.auth.SessionManager;
import com.gawdscape.json.auth.SessionResponse;
import com.gawdscape.launcher.updater.Updater;
import com.gawdscape.launcher.launch.MinecraftLauncher;
import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.JsonUtils;
import com.gawdscape.launcher.util.Log;
import com.gawdscape.launcher.util.OperatingSystem;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

/**
 *
 * @author Vinnie
 */
public class GawdScapeLauncher {

	public static Config config;
	public static LogFrame logFrame;
	public static LauncherFrame launcherFrame;
	public static LoginDialog loginDialog;
	public static SessionResponse session;
	public static Updater updater;
	public static MinecraftLauncher launcher;
	public static ModPackManager modpacks;
	public static boolean offlineMode;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		long startTime = System.currentTimeMillis();

		/* Set the System look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code ">
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(LauncherFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
	//</editor-fold>

		// Load config
		long startTime2 = System.currentTimeMillis();
		Log.info("Loading config...");
		config = Config.loadConfig();
		if (config == null) {
			config = new Config();
		}
		Log.benchmark("Config took ", startTime2);

		/* Create and display the form */
		startTime2 = System.currentTimeMillis();
		if (config.getShowLog()) {
			Log.info("Initializing Log.");
			Log.showLog = config.getShowLog();
			logFrame = new LogFrame(config.getColorLog(), config.getLinkLog());
			logFrame.setVisible(true);
		}
		Log.benchmark("Log took ", startTime2);

		// Get list of mod packs
		startTime2 = System.currentTimeMillis();
		modpacks = new ModPackManager();

		try {
			modpacks.downloadPacks();
		} catch (UnknownHostException e) {
			offlineMode = true;
			Log.severe("Error connecting to mod pack server.");
		} catch (IOException ex) {
			Log.error("Error downloading mod pack index.", ex);
		}

		try {
			modpacks.loadCustomPacks();
		} catch (IOException ex) {
			Log.error("Error loading custom mod pack index.", ex);
		}
		Log.benchmark("Mod Packs took ", startTime2);

		if (offlineMode) {
			try {
				modpacks.loadLocalPacks();
				launcherFrame = new LauncherFrame();
				launcherFrame.setVisible(true);
				Log.benchmark("Opened GawdScape Launcher in ", startTime);
			} catch (IOException ex) {
				Log.error("Error loading local mod pack index.", ex);
			}
			return;
		}
		

		SessionManager sessionManager = SessionManager.loadSessions();

		// Load and refresh last saved session
		Log.info("Checking for saved session...");
		if (sessionManager.shouldAutoLogin()) {
			startTime2 = System.currentTimeMillis();
			Log.info("Session found. Refreshing session for " + sessionManager.getAutoLoginUser());
			session = AuthManager.refresh(sessionManager.getAutoLoginToken());
			sessionManager.addSession(session);
			SessionManager.saveSessions(sessionManager);
			Log.benchmark("Auto Login took ", startTime2);
		}


		startTime2 = System.currentTimeMillis();
		// Are we logged in?
		if (session != null && session.getAccessToken() != null) {
			// Valid session, continue
			Log.info("Refreshed session for " + session.getSelectedProfile().getName());
			// Should we skip the launcher and just launch Minecraft?
			if (config.getSkipLauncher()) {
				updater = new Updater();
				updater.setPack(modpacks.getPackById(config.getDefaultPack()));
				updater.start();
			} else {
				// Initalize launcher frame
				launcherFrame = new LauncherFrame();
				launcherFrame.setUsername(session.getSelectedProfile().getName());
				launcherFrame.setVisible(true);
			}
		} else {
			// Nope, Login
			launcherFrame = new LauncherFrame();
			loginDialog = new LoginDialog(launcherFrame, true, sessionManager);
			if (session != null && session.getError() != null) {
				loginDialog.setError(session.getErrorMessage());
				Log.severe("Error refreshing session:");
				Log.severe(session.getErrorMessage());
			}
			loginDialog.setVisible(true);
		}
		Log.benchmark("Login Check took ", startTime2);

		checkLauncherUpdate();

		Log.benchmark("Opened GawdScape Launcher in ", startTime);
	}

	public static void checkLauncherUpdate() {
		Log.info("Checking for launcher update...");
		try {
			int version = new Integer(
					JsonUtils.readJsonFromUrl(Constants.LAUNCHER_VERSION_URL));
			if (version > Constants.VERSION) {
				Log.warning("Launcher version " + version + " has been released.");
				promptUpdate(version);
			} else {
				Log.info("No launcher update found.");
			}
		} catch (IOException ex) {
			Log.error("Error checking for launcher update.", ex);
		}
	}

	public static void promptUpdate(int version) {
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
