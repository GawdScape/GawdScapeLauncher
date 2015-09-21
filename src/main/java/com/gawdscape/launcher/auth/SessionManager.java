package com.gawdscape.launcher.auth;

import com.gawdscape.json.auth.RefreshRequest;
import com.gawdscape.json.auth.SessionResponse;
import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.JsonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

/**
 *
 * @author Vinnie
 */
public class SessionManager {

    private static final File userFile = new File(Directories.getWorkingDirectory(), "sessions.json");

    private final HashMap<String, RefreshRequest> sessions;
    private String lastUser;
    private String autoLoginUser;

    public SessionManager() {
	sessions = new HashMap<>();
    }

    public RefreshRequest addSession(SessionResponse response) {
	return sessions.put(response.getSelectedProfile().getName(),
		new RefreshRequest(response));
    }

    public RefreshRequest getToken(String username) {
	return sessions.get(username);
    }

    public RefreshRequest removeSession(String username) {
	return sessions.remove(username);
    }

    public boolean isUserSaved(String username) {
	return sessions.containsKey(username);
    }

    public Set<String> getSavedUsernames() {
	return sessions.keySet();
    }

    public void setLastUser(String username) {
	lastUser = username;
    }

    public String getLastUser() {
	return lastUser;
    }

    public void setAutoLoginUser(String username) {
	autoLoginUser = username;
    }

    public String getAutoLoginUser() {
	return autoLoginUser;
    }

    public RefreshRequest getAutoLoginToken() {
	return sessions.get(autoLoginUser);
    }

    public boolean isAutoLoginUser(String username) {
	return autoLoginUser != null && autoLoginUser.equals(username);
    }

    public boolean shouldAutoLogin() {
	return autoLoginUser != null && sessions.containsKey(autoLoginUser);
    }

    public static SessionManager loadSessions() {
	String sessionJson = "";
	try {
	    sessionJson = JsonUtils.readJsonFromFile(userFile);
	} catch (FileNotFoundException ex) {
	    GawdScapeLauncher.logger.warning("Saved sessions not found.");
	} catch (IOException ex) {
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Error loading saved sessions.", ex);
	}
	SessionManager manager = JsonUtils.getGson().fromJson(sessionJson, SessionManager.class);
	if (manager == null) {
	    manager = new SessionManager();
	}
	return manager;
    }

    public static void saveSessions(SessionManager manager) {
	String sessionJson = JsonUtils.getGson().toJson(manager);
	try {
	    JsonUtils.writeJsonToFile(sessionJson, userFile);
	} catch (IOException ex) {
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Error saving sessions.", ex);
	}
    }

    public static void clearSavedSessions() {
	String userJson = "{}";
	try {
	    JsonUtils.writeJsonToFile(userJson, userFile);
	} catch (IOException ex) {
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Error saving last login", ex);
	}
    }
}
