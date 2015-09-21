package com.gawdscape.json.auth;

/**
 *
 * @author Vinnie
 */
public class RefreshRequest {

    private final String accessToken;
    private final String clientToken;
    private final boolean requestUser = true;

    public RefreshRequest(String access, String client) {
	this.accessToken = access;
	this.clientToken = client;
    }

    public RefreshRequest(SessionResponse response) {
	this.accessToken = response.getAccessToken();
	this.clientToken = response.getClientToken();
    }

    public String getAccessToken() {
	return this.accessToken;
    }

    public String getClientToken() {
	return this.clientToken;
    }

}
