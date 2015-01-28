package com.gawdscape.launcher.auth;

/**
 *
 * @author Vinnie
 */
public class Profile {

	private String id;
	private String name;
	private boolean legacy;

	public Profile(String id, String name) {
		this.id = id;
		this.name = name;
		this.legacy = false;
	}

	public Profile(String id, String name, boolean legacy) {
		this.id = id;
		this.name = name;
		this.legacy = legacy;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public boolean isLegacy() {
		return this.legacy;
	}
}
