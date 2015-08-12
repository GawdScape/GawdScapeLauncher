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
		return this.error;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public String getCause() {
		return this.cause;
	}
}
