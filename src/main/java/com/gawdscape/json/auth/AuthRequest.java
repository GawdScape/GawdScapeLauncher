package com.gawdscape.json.auth;

/**
 *
 * @author Vinnie
 */
public class AuthRequest {

    private final Agent agent;
    private final String username;
    private final String password;
    private final String clientToken;
    private final boolean requestUser = true;

    public AuthRequest(Agent agent, String username, String password, String clientToken) {
	this.agent = agent;
	this.username = username;
	this.password = password;
	this.clientToken = clientToken;
    }

    public String getUsername() {
	return username;
    }

    public String getClientToken() {
	return clientToken;
    }
}
