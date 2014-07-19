package com.gawdscape.launcher;

import com.gawdscape.launcher.auth.AuthManager;
import com.gawdscape.launcher.auth.SessionResponse;
import com.gawdscape.launcher.download.Updater;
import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.JsonUtils;
import com.gawdscape.launcher.util.Log;
import com.gawdscape.launcher.util.OperatingSystem;
import com.gawdscape.launcher.auth.SessionManager;
import java.io.IOException;
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
    public static SessionResponse response;

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
	/* Set the System look and feel */
	//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code ">
	try {
	    javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(LauncherFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>

	// Load config
	System.out.println("Loading config.");
	config = Config.loadConfig();
	if (config == null) {
	    config = new Config();
	}

	// Load and refresh last saved session
	System.out.println("Checking for saved session...");
	SessionResponse lastSession = SessionManager.loadSession();
	if (lastSession != null && lastSession.getAccessToken() != null) {
	    System.out.println("Session found. Refreshing...");
	    response = AuthManager.refresh(lastSession.getAccessToken(), lastSession.getClientToken());
	    SessionManager.saveSession(response);
	}

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(new Runnable() {
	    public void run() {
		if (config.getShowLog()) {
		    System.out.println("Initializing Log.");
		    new Log().start();
		}

		if (config.getSkipLauncher() && (response != null && response.getAccessToken() != null)) {
		    Log.info("Refreshed session for " + response.getSelectedProfile().getName());
		    Updater updater = new Updater();
		    updater.start();
		} else {
		    // Initalize launcher frame
		    launcherFrame = new LauncherFrame();

		    // Valid session, open launcher
		    if (response != null && response.getAccessToken() != null) {
			Log.info("Refreshed session for " + response.getSelectedProfile().getName());
			launcherFrame.setUsername(response.getSelectedProfile().getName());
			launcherFrame.setVisible(true);
			// Login, open login form
		    } else {
			loginDialog = new LoginDialog(launcherFrame, true);
			if (response != null && response.getError() != null) {
			    loginDialog.setError(response.getErrorMessage());
			    Log.severe("Error refreshing session:");
			    Log.severe(response.getErrorMessage());
			}
			loginDialog.setVisible(true);
		    }
		}
	    }
	});

	checkLauncherUpdate();
    }

    public static void checkLauncherUpdate() {
	Log.info("Checking for update...");
	int version = 0;
	try {
	    version = new Integer(JsonUtils.readJsonFromUrl(Constants.LAUNCHER_VERSION_URL));
	} catch (IOException ex) {
	    Log.error("Error checking for launcher update.", ex);
	}
	if (version > Constants.VERSION) {
	    Log.warning("Launcher version " + version + " has been released.");
	    promptUpdate(version);
	} else {
	    Log.fine("No update found.");
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
