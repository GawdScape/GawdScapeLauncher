package com.gawdscape.json.auth;

/**
 *
 * @author Vinnie
 */
public class Profile {

    private final String id;
    private final String name;
    private final boolean legacy;

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
	return id;
    }

    public String getName() {
	return name;
    }

    public boolean isLegacy() {
	return legacy;
    }
}
