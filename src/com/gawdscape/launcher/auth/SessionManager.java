package com.gawdscape.launcher.auth;

import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.JsonUtils;
import com.gawdscape.launcher.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Vinnie
 */
public class SessionManager {

	private static final File userFile = new File(Directories.getWorkingDirectory(), "sessions.json");

	private final HashMap<String, SessionToken> sessions = new HashMap();
	private String lastUser;
	private String autoLoginUser;

	public SessionManager() {
	}

	public SessionToken addSession(SessionResponse response) {
		return sessions.put(response.getSelectedProfile().getName(), new SessionToken(response));
	}

	public SessionToken getToken(String username) {
		return sessions.get(username);
	}

	public SessionToken removeSession(String username) {
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

	public SessionToken getAutoLoginToken() {
		return sessions.get(autoLoginUser);
	}

	public boolean isAutoLoginUser(String username) {
		if (autoLoginUser == null) {
			return false;
		}
		return autoLoginUser.equals(username);
	}

	public boolean shouldAutoLogin() {
		if (autoLoginUser != null) {
			return sessions.containsKey(autoLoginUser);
		}
		return false;
	}

	public static SessionManager loadSessions() {
		String sessionJson = "";
		try {
			sessionJson = JsonUtils.readJsonFromFile(userFile);
		} catch (FileNotFoundException ex) {
			Log.warning("Saved sessions not found.");
		} catch (IOException ex) {
			Log.error("Error loading saved sessions.", ex);
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
			Log.error("Error saving sessions.", ex);
		}
	}

	public static void clearSavedSessions() {
		String userJson = "{}";
		try {
			JsonUtils.writeJsonToFile(userJson, userFile);
		} catch (IOException ex) {
			Log.error("Error saving last login", ex);
		}
	}
}
