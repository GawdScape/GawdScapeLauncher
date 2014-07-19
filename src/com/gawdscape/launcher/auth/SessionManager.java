package com.gawdscape.launcher.auth;

import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.JsonUtils;
import com.gawdscape.launcher.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Vinnie
 */
public class SessionManager {

    private static File userFile = new File(Directories.getWorkingDirectory(), "session.json");

    public static SessionResponse loadSession() {
	String userJson = "";
	try {
	    userJson = JsonUtils.readJsonFromFile(userFile);
	} catch (FileNotFoundException ex) {
	    Log.error("Last login not found", ex);
	} catch (IOException ex) {
	    Log.error("Error saving last login", ex);
	}
	SessionResponse user = JsonUtils.getGson().fromJson(userJson, SessionResponse.class);
	return user;
    }

    public static void saveSession(SessionResponse profile) {
	String userJson = JsonUtils.getGson().toJson(profile);
	try {
	    JsonUtils.writeJsonToFile(userJson, userFile);
	} catch (IOException ex) {
	    Log.error("Error saving last login", ex);
	}
    }

    public static void clearSavedSession() {
	String userJson = "{}";
	try {
	    JsonUtils.writeJsonToFile(userJson, userFile);
	} catch (IOException ex) {
	    Log.error("Error saving last login", ex);
	}
    }
}
