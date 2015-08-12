package com.gawdscape.json.auth;

import com.google.gson.JsonElement;

/**
 *
 * @author Vinnie
 */
public class User {

	private String id;
	private JsonElement properties;

	public String getId() {
		return id;
	}

	public JsonElement getProperties() {
		return properties;
	}
}
