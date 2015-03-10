/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gawdscape.launcher.game;

import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.JsonUtils;
import com.gawdscape.launcher.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Vinnie
 */
public class PackIndex {
	public final HashMap<String, String> packs = new HashMap<>();

	public PackIndex() {
	}

    public Set<String> getIDs() {
        return packs.keySet();
    }

	public void loadPacks() {
		Log.info("Loading Mod Packs...");
		JsonParser parser = new JsonParser();
		try {
			JsonArray a = (JsonArray) parser.parse(JsonUtils.readJsonFromUrl(Constants.GS_PACK_URL + "Index/packs.json"));
			for (Object o : a) {
				JsonObject pack = (JsonObject) o;
				packs.put(pack.get("id").getAsString(), pack.get("version").getAsString());
			}
		} catch (IOException e) {
			Log.error("Error reading packs.json.", e);
		}
	}
}
