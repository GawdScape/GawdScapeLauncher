package com.gawdscape.launcher.game;

import com.gawdscape.launcher.util.Constants;

/**
 *
 * @author Vinnie
 *
 * Not yet implemented
 */
public class Mod {

	private String name;
	private ModType type;
	private ExtractRules extract;
	private String url;

	public Mod() {
	}

	public Mod(String name) {
		if ((name == null) || (name.length() == 0)) {
			throw new IllegalArgumentException("Mod name cannot be null or empty");
		}
		this.name = name;
		this.type = ModType.FORGE;
	}

	public Mod(Mod mod) {
		name = mod.name;
		type = mod.getType();
		url = mod.url;
		if (mod.extract != null) {
			extract = new ExtractRules(mod.extract);
		}
	}

	public String getName() {
		return name;
	}

	public ModType getType() {
		return type;
	}

	public ExtractRules getExtractRules() {
		return extract;
	}

	public Mod setExtractRules(ExtractRules rules) {
		extract = rules;
		return this;
	}

	public String getArtifactBaseDir() {
		if (name == null) {
			throw new IllegalStateException("Cannot get artifact dir of empty/blank artifact");
		}
		String[] parts = name.split(":", 3);
		return String.format("%s/%s/%s", new Object[]{parts[0].replaceAll("\\.", "/"), parts[1], parts[2]});
	}

	public String getArtifactPath() {
		return getArtifactPath(null);
	}

	public String getArtifactPath(String classifier) {
		if (name == null) {
			throw new IllegalStateException("Cannot get artifact path of empty/blank artifact");
		}
		return String.format("%s/%s", new Object[]{getArtifactBaseDir(), getArtifactFilename(classifier)});
	}

	public String getArtifactFilename(String classifier) {
		if (name == null) {
			throw new IllegalStateException("Cannot get artifact filename of empty/blank artifact");
		}
		if (classifier != null) {
			classifier = "-" + classifier;
		} else {
			classifier = "";
		}
		String extension = "jar";
		if (type == ModType.LITEMOD) {
			extension = "litemod";
		}
		String[] parts = name.split(":", 3);
		return String.format("%s-%s%s.%s", new Object[]{parts[1], parts[2], classifier, extension});
	}

	public String toString() {
		return "Mod{name='" + name + "'" + ", type=" + type + ", extract=" + extract + "}";
	}

	public boolean hasCustomUrl() {
		return url != null;
	}

	public String getDownloadUrl() {
		if (url != null) {
			return url;
		}
		return Constants.GS_STORAGE_URL + "Mods/";
	}
}
