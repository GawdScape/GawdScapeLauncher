package com.gawdscape.json.modpacks;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author Vinnie
 */
public class PackIndexCustom {

    private final Map<String, String> packs;

    public PackIndexCustom(Map<String, String> packs) {
	this.packs = packs;
    }

    public Map<String, String> getPackMap() {
	return packs;
    }

    public Set<String> getModPacks() {
	return packs.keySet();
    }

    public String getPackUrl(String name) {
	return packs.get(name);
    }

    public void addPack(String name, String url) {
	packs.put(name, url);
    }

    public void removePack(String name) {
	packs.remove(name);
    }
}
