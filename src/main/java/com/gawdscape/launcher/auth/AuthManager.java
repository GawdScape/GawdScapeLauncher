package com.gawdscape.launcher.auth;

import com.gawdscape.json.auth.AuthRequest;
import com.gawdscape.json.auth.RefreshRequest;
import com.gawdscape.json.auth.SessionResponse;
import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.launcher.util.Constants;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;

/**
 *
 * @author Vinnie
 */
public class AuthManager {

    private static final Gson gson = new Gson();

    // Login with username and password
    public static SessionResponse authenticate(String username, String password, String clientToken) {
	AuthRequest request = new AuthRequest(Constants.MINECRAFT, username, password, clientToken);
	String result = postJson(Constants.MC_LOGIN, gson.toJson(request));
	GawdScapeLauncher.logger.log(Level.FINE, "Authentication: {0}", result);
	return gson.fromJson(result, SessionResponse.class);
    }

    // Login with UUID and ClientToken
    public static SessionResponse refresh(RefreshRequest request) {
	String result = postJson(Constants.MC_REFRESH, gson.toJson(request));
	GawdScapeLauncher.logger.log(Level.FINE, "Refresh: {0}", result);
	return gson.fromJson(result, SessionResponse.class);
    }

    private static String postJson(URL url, String json) {
	try {
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    byte[] payloadAsBytes = json.getBytes("UTF-8");

	    conn.setConnectTimeout(Constants.MC_AUTH_TIMEOUT);
	    conn.setReadTimeout(Constants.MC_AUTH_TIMEOUT);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
	    conn.setRequestProperty("Content-Length", String.valueOf(payloadAsBytes.length));
	    conn.setUseCaches(false);
	    conn.setDoInput(true);
	    conn.setDoOutput(true);

	    try (DataOutputStream outStream = new DataOutputStream(conn.getOutputStream())) {
		outStream.write(payloadAsBytes);
		outStream.flush();
	    }

	    try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
		StringBuilder response = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
		    response.append(line);
		}
		return response.toString();
	    }
	} catch (UnknownHostException e) {
	    GawdScapeLauncher.logger.severe("Error connecting to authentication server.");
	} catch (IOException e) {
	    GawdScapeLauncher.logger.log(Level.SEVERE, "Authentication Error: {0}", e.getMessage());
	}
	return null;
    }
}
