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
	return accessToken;
    }

    // Client UUID
    public String getClientToken() {
	return clientToken;
    }

    public Profile[] getAvailableProfiles() {
	return availableProfiles;
    }

    public Profile getSelectedProfile() {
	return selectedProfile;
    }

    public User getUser() {
	return user;
    }

    // Session Token
    public String getSessionId() {
	return "token:" + accessToken + ":" + selectedProfile.getId();
    }
}
