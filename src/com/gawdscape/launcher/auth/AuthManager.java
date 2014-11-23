package com.gawdscape.launcher.auth;

import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.JsonUtils;
import com.gawdscape.launcher.util.Log;
import com.google.gson.Gson;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 *
 * @author Vinnie
 */
public class AuthManager {
    private static final Gson gson = JsonUtils.getGson();

    // Login with username and password
    public static SessionResponse authenticate(String username, String password, String clientToken) {
	AuthRequest request = new AuthRequest(Constants.MINECRAFT, username, password, clientToken);
	String result = postJson(Constants.MC_AUTH, gson.toJson(request));
	Log.fine("Authentication: " + result);
	SessionResponse response = gson.fromJson(result, SessionResponse.class);
	return response;
    }

    // Login with UUID and ClientToken
    public static SessionResponse refresh(String accessToken, String clientToken) {
	RefreshRequest request = new RefreshRequest(accessToken, clientToken);
	String result = postJson(Constants.MC_REFRESH, gson.toJson(request));
	Log.fine("Refresh: " + result);
	SessionResponse response = gson.fromJson(result, SessionResponse.class);
	return response;
    }

    private static String postJson(URL url, String json) {
	try {
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    byte[] payloadAsBytes = json.getBytes(Charset.forName("UTF-8"));

	    conn.setConnectTimeout(15000);
	    conn.setReadTimeout(15000);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
	    conn.setRequestProperty("Content-Length", "" + payloadAsBytes.length);
	    conn.setUseCaches(false);
	    conn.setDoInput(true);
	    conn.setDoOutput(true);

	    DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
	    outStream.write(payloadAsBytes);
	    outStream.flush();
	    outStream.close();

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

	} catch (IOException e) {
	    Log.error("Authentication Error: ", e);
	}
	return null;
    }
}
