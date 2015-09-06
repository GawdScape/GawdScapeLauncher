package com.gawdscape.launcher.auth;

import com.gawdscape.json.auth.RefreshRequest;
import com.gawdscape.json.auth.AuthRequest;
import com.gawdscape.json.auth.SessionResponse;
import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.Log;
import com.google.gson.Gson;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

/**
 *
 * @author Vinnie
 */
public class AuthManager {

	private static final Gson gson = new Gson();

	// Login with username and password
	public static SessionResponse authenticate(String username, String password, String clientToken) {
		AuthRequest request = new AuthRequest(Constants.MINECRAFT, username, password, clientToken);
		String result = postJson(Constants.MC_AUTH, gson.toJson(request));
		Log.finer("Authentication: " + result);
		return gson.fromJson(result, SessionResponse.class);
	}

	// Login with UUID and ClientToken
	public static SessionResponse refresh(RefreshRequest request) {
		String result = postJson(Constants.MC_REFRESH, gson.toJson(request));
		Log.finer("Refresh: " + result);
		return gson.fromJson(result, SessionResponse.class);
	}

	private static String postJson(URL url, String json) {
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			byte[] payloadAsBytes = json.getBytes("UTF-8");

			conn.setConnectTimeout(15000);
			conn.setReadTimeout(15000);
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

			InputStream inStream;
			try {
				inStream = conn.getInputStream();
			} catch (Exception e) {
				inStream = conn.getErrorStream();
			}

			StringBuilder response = new StringBuilder();
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = inStream.read(buffer)) > 0) {
				response.append(new String(buffer, "UTF-8").substring(0, bytesRead));
			}
			return response.toString();
		} catch (UnknownHostException e) {
			Log.severe("Error connecting to authentication server.");
		} catch (IOException e) {
			Log.error("Authentication Error: ", e);
		}
		return null;
	}
}
