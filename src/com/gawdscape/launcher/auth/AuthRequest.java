package com.gawdscape.launcher.auth;

/**
 *
 * @author Vinnie
 */
public class AuthRequest {

	private Agent agent;
	private String username;
	private String password;
	private String clientToken;
	private boolean requestUser = true;

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
