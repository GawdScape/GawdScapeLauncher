package com.gawdscape.json.auth;

/**
 *
 * @author Vinnie
 */
public class SessionResponse extends ErrorResponse {

	private String accessToken;
	private String clientToken;
	private Profile[] availableProfiles;
	private Profile selectedProfile;
	private User user;

	// User UUID
	public String getAccessToken() {
		return this.accessToken;
	}

	// Client UUID
	public String getClientToken() {
		return this.clientToken;
	}

	public Profile[] getAvailableProfiles() {
		return this.availableProfiles;
	}

	public Profile getSelectedProfile() {
		return this.selectedProfile;
	}

	public User getUser() {
		return user;
	}

	// Session Token
	public String getSessionId() {
		return "token:" + this.accessToken + ":" + this.selectedProfile.getId();
	}
}
