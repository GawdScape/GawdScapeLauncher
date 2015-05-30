package com.gawdscape.json.modpacks;

import java.util.List;

/**
 *
 * @author Vinnie
 */
public class PackIndex {

	private final List<String> packs;

	public PackIndex(List<String> packs) {
		this.packs = packs;
	}

	public List<String> getModPacks() {
		return packs;
	}
}
