package com.gawdscape.json.auth;

/**
 *
 * @author Vinnie
 */
public class ErrorResponse {

    private final String error = "";
    private final String errorMessage = "";
    private final String cause = "";

    public String getError() {
	return error;
    }

    public String getErrorMessage() {
	return errorMessage;
    }

    public String getCause() {
	return cause;
    }
}
