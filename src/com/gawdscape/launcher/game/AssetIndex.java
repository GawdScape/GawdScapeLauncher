package com.gawdscape.launcher.game;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Vinnie
 */
public class AssetIndex {

	private Map<String, AssetObject> objects;
	private boolean virtual;

	public AssetIndex() {
		objects = new LinkedHashMap();
	}

	public Map<String, AssetObject> getFileMap() {
		return objects;
	}

	public Set<AssetObject> getUniqueObjects() {
		return new HashSet(objects.values());
	}

	public boolean isVirtual() {
		return virtual;
	}

	public class AssetObject {

		private String hash;
		private long size;

		public AssetObject() {
		}

		public String getHash() {
			return hash;
		}

		public long getSize() {
			return size;
		}
	}
}
