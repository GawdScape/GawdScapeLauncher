package com.gawdscape.json.auth;

/**
 *
 * @author Vinnie
 */
public class RefreshRequest {

    private final String accessToken;
    private final String clientToken;
    private final boolean requestUser = true;

    public RefreshRequest(String accessToken, String clientToken) {
	this.accessToken = accessToken;
	this.clientToken = clientToken;
    }

    public RefreshRequest(SessionResponse response) {
	accessToken = response.getAccessToken();
	clientToken = response.getClientToken();
    }

    public String getAccessToken() {
	return accessToken;
    }

    public String getClientToken() {
	return clientToken;
    }

}
