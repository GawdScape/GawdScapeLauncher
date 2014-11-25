package com.gawdscape.launcher.auth;

import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.JsonUtils;
import com.gawdscape.launcher.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
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

    public Collection<SessionToken> getSavedSessions() {
	return sessions.values();
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

    public boolean isAutoLoginUserSaved() {
	return sessions.containsKey(autoLoginUser);
    }

    public SessionToken getAutoLoginToken() {
	return sessions.get(autoLoginUser);
    }

    public static SessionManager loadSessions() {
	String userJson = "";
	try {
	    userJson = JsonUtils.readJsonFromFile(userFile);
	} catch (FileNotFoundException ex) {
	    Log.error("Last login not found", ex);
	} catch (IOException ex) {
	    Log.error("Error saving last login", ex);
	}
	SessionManager user = JsonUtils.getGson().fromJson(userJson, SessionManager.class);
	return user;
    }

    public static void saveSessions(SessionManager profile) {
	String userJson = JsonUtils.getGson().toJson(profile);
	try {
	    JsonUtils.writeJsonToFile(userJson, userFile);
	} catch (IOException ex) {
	    Log.error("Error saving last login", ex);
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
