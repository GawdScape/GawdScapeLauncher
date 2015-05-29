package com.gawdscape.launcher;

import com.gawdscape.json.modpacks.PackIndex;
import com.gawdscape.json.modpacks.PackIndexCustom;
import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.JsonUtils;
import com.gawdscape.launcher.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Vinnie
 */
public class ModPackManager {
	public static final String packURL = "http://localhost/packIndex.json";//Constants.GS_PACK_URL + "Index/packs.json";
	public static final File packFile = new File(Directories.getPackDataPath(), "packIndex.json");
	public static final File customPackFile = new File(Directories.getPackDataPath(), "packIndexCustom.json");
	public final HashMap<String, String> packs;

	public ModPackManager() {
		packs = new HashMap();
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
				(pack) -> {packs.put(pack, Constants.GS_PACK_URL + pack);}
		);
		savePacks(index);
	}

	public void loadLocalPacks() throws IOException {
		if (!packFile.exists())
			return;
		String json = JsonUtils.readJsonFromFile(packFile);
		PackIndex index = JsonUtils.getGson().fromJson(json, PackIndex.class);
		index.getModPacks().stream().forEach(
				(pack) -> {packs.put(pack, Constants.GS_PACK_URL + pack);}
		);
	}

	public void loadCustomPacks() throws IOException {
		if (!customPackFile.exists())
			return;
		String json = JsonUtils.readJsonFromFile(customPackFile);
		PackIndexCustom index = JsonUtils.getGson().fromJson(json, PackIndexCustom.class);
		packs.putAll(index.getPackMap());
	}

	public static void savePacks(PackIndex index) {
		try {
			String json = JsonUtils.getGson().toJson(index);
			JsonUtils.writeJsonToFile(json, packFile);
		} catch (IOException ex) {
			Log.error("Error saving mod packs.", ex);
		}
	}

	public static void saveCustomPacks(PackIndexCustom index) {
		try {
			String json = JsonUtils.getGson().toJson(index);
			JsonUtils.writeJsonToFile(json, customPackFile);
		} catch (IOException ex) {
			Log.error("Error saving custom mod packs.", ex);
		}
	}

	public void addCustomPack(String name, String url) throws IOException {
		PackIndexCustom index;
		if (customPackFile.exists()) {
			String json = JsonUtils.readJsonFromFile(customPackFile);
			index = JsonUtils.getGson().fromJson(json, PackIndexCustom.class);
		} else {
			index = new PackIndexCustom(new HashMap());
		}
		index.addPack(name, url);
		saveCustomPacks(index);
	}

	public String getPackUrl(String name) {
		return packs.get(name);
	}

	public String getPackById(int id) {
		return (String) packs.keySet().toArray()[id];
	}
}
