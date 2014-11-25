package com.gawdscape.launcher.auth;

/**
 *
 * @author Vinnie
 */
public class SessionToken {
    private final String accessToken;
    private final String clientToken;

    public SessionToken(String accessToken, String clientToken) {
	this.accessToken = accessToken;
	this.clientToken = clientToken;
    }

    public SessionToken(SessionResponse response) {
	this.accessToken = response.getAccessToken();
	this.clientToken = response.getClientToken();
    }

    public String getAccessToken() {
	return accessToken;
    }

    public String getClientToken() {
	return clientToken;
    }
}
