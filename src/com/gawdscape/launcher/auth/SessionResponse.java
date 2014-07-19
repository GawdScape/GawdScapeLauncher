package com.gawdscape.launcher.auth;

/**
 *
 * @author Vinnie
 */
public class SessionResponse extends ErrorResponse {

    private String accessToken;
    private String clientToken;
    private Profile[] availableProfiles;
    private Profile selectedProfile;

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

    // Session Token
    public String getSessionId() {
	return "token:" + this.accessToken + ":" + this.selectedProfile.getId();
    }
}
