package com.gawdscape.launcher;

import com.gawdscape.json.modpacks.PackIndex;
import com.gawdscape.json.modpacks.PackIndexCustom;
import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

/**
 *
 * @author Vinnie
 */
public class ModPackManager {

    private static final String packURL = Constants.GS_PACK_URL + "Index/packs.json";
    private static final File packFile = new File(Directories.getPackDataPath(), "packIndex.json");
    private static final File customPackFile = new File(Directories.getPackDataPath(), "packIndexCustom.json");
    private final HashMap<String, String> packs;

    public ModPackManager() {
	packs = new HashMap<>();
    }

    public int getPackCount() {
	return packs.size();
    }

    public Set<String> getPacks() {
	return packs.keySet();
    }

    public void downloadPacks() throws IOException {
	String json = JsonUtils.readJsonFromUrl(packURL);
	PackIndex index = JsonUtils.getGson().fromJson(json, PackIndex.class);
	index.getModPacks().stream().forEach(
		(pack) -> packs.put(pack, Constants.GS_PACK_URL + pack)
	);
	savePacks(index);
    }

    public void loadLocalPacks() throws IOException {
	if (!packFile.exists()) {
	    return;
	}
	String json = JsonUtils.readJsonFromFile(packFile);
	PackIndex index = JsonUtils.getGson().fromJson(json, PackIndex.class);
	index.getModPacks().stream().forEach(
		(pack) -> packs.put(pack, Constants.GS_PACK_URL + pack)
	);
    }

    public void loadCustomPacks() throws IOException {
	if (!customPackFile.exists()) {
	    return;
	}
	String json = JsonUtils.readJsonFromFile(customPackFile);
	PackIndexCustom index = JsonUtils.getGson().fromJson(json, PackIndexCustom.class);
	packs.putAll(index.getPackMap());
    }

    private static void savePacks(PackIndex index) {
	try {
	    String json = JsonUtils.getGson().toJson(index);
	    JsonUtils.writeJsonToFile(json, packFile);
	} catch (IOException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Error saving mod packs.", ex);
	}
    }

    private static void saveCustomPacks(PackIndexCustom index) {
	try {
	    String json = JsonUtils.getGson().toJson(index);
	    JsonUtils.writeJsonToFile(json, customPackFile);
	} catch (IOException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Error saving custom mod packs.", ex);
	}
    }

    public void addCustomPack(String name, String url) throws IOException {
	PackIndexCustom index;
	if (customPackFile.exists()) {
	    String json = JsonUtils.readJsonFromFile(customPackFile);
	    index = JsonUtils.getGson().fromJson(json, PackIndexCustom.class);
	} else {
	    index = new PackIndexCustom(new HashMap<>());
	}
	index.addPack(name, url);
	saveCustomPacks(index);
	packs.put(name, url);
    }

    public String getPackUrl(String name) {
	return packs.get(name);
    }

    public String getPackById(int id) {
	return (String) packs.keySet().toArray()[id];
    }

    public void removeCustomPack(String name) throws IOException {
	PackIndexCustom index;
	if (customPackFile.exists()) {
	    String json = JsonUtils.readJsonFromFile(customPackFile);
	    index = JsonUtils.getGson().fromJson(json, PackIndexCustom.class);
	} else {
	    index = new PackIndexCustom(new HashMap<>());
	}
	index.removePack(name);
	saveCustomPacks(index);
	packs.remove(name);
    }
}
