package com.gawdscape.json.auth;

/**
 *
 * @author Vinnie
 */
public class Agent {

    private final String name;
    private final int version;

    public Agent(String name, int version) {
	this.name = name;
	this.version = version;
    }

    public String getName() {
	return this.name;
    }

    public int getVersion() {
	return this.version;
    }
}
