package com.gawdscape.launcher.game;

import com.gawdscape.launcher.util.Directories;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.gawdscape.launcher.util.OperatingSystem;

/**
 *
 * @author Vinnie
 */
public class ModPack {

	private String id;
	private String version;
	private String minecraftVersion;
	private String gawdmodVersion;
	private String mainClass;
	private String minecraftArguments;
	private List<Library> libraries;
	private List<Mod> mods;

	public ModPack() {
	}

	public ModPack(String id, String version, String minecraftVersion, String gawdmodVersion, String mainClass, String minecraftArguments) {
		if ((id == null) || (id.length() == 0)) {
			throw new IllegalArgumentException("ID cannot be null or empty");
		}
		if ((version == null) || (version.length() == 0)) {
			throw new IllegalArgumentException("Version cannot be null");
		}
		if ((minecraftVersion == null) || (minecraftVersion.length() == 0)) {
			throw new IllegalArgumentException("Minecraft version cannot be null");
		}
		this.id = id;
		this.minecraftVersion = minecraftVersion;
		this.gawdmodVersion = gawdmodVersion;
		this.mainClass = mainClass;
		this.minecraftArguments = minecraftArguments;
		libraries = new ArrayList();
		mods = new ArrayList();
	}

	public ModPack(ModPack version) {
		this(version.getId(), version.getVersion(), version.getMinecraftVersion(), version.getGawdModVersion(), version.getMainClass(), version.getMinecraftArguments());
		for (Library lib : version.getLibraries()) {
			libraries.add(new Library(lib));
		}
		for (Mod mod : version.getMods()) {
			mods.add(new Mod(mod));
		}
	}

	public String getId() {
		return id;
	}

	public String getVersion() {
		return version;
	}

	public String getMinecraftVersion() {
		return minecraftVersion;
	}

	public String getGawdModVersion() {
		return gawdmodVersion;
	}

	public List<Library> getLibraries() {
		return libraries;
	}

	public Collection<Library> getRelevantLibraries() {
		List<Library> result = new ArrayList();
		if (libraries == null) {
			return result;
		}
		for (Library library : libraries) {
			if (library.appliesToCurrentEnvironment()) {
				result.add(library);
			}
		}
		return result;
	}

	public List<Mod> getMods() {
		return mods;
	}

	public String getMainClass() {
		return mainClass;
	}

	public String getMinecraftArguments() {
		return minecraftArguments;
	}

	public Collection<File> getClassPath() {
		Collection<Library> libs = getRelevantLibraries();
		Collection<File> result = new ArrayList();
		for (Library lib : libs) {
			if (lib.getNatives() == null) {
				result.add(new File(Directories.getLibraryPath(), lib.getArtifactPath()));
			}
		}
		return result;
	}

	public Collection<String> getExtractFiles(OperatingSystem os) {
		Collection<Library> libs = getRelevantLibraries();
		Collection<String> result = new ArrayList();
		for (Library lib : libs) {
			Map<OperatingSystem, String> natives = lib.getNatives();
			if ((natives != null) && (natives.containsKey(os))) {
				result.add("libraries/" + lib.getArtifactPath((String) natives.get(os)));
			}
		}
		return result;
	}

	public Set<String> getRequiredFiles(OperatingSystem os) {
		Set<String> neededFiles = new HashSet();
		for (Library lib : getRelevantLibraries()) {
			if (lib.getNatives() != null) {
				String natives = (String) lib.getNatives().get(os);
				if (natives != null) {
					neededFiles.add("libraries/" + lib.getArtifactPath(natives));
				}
			} else {
				neededFiles.add("libraries/" + lib.getArtifactPath());
			}
		}
		return neededFiles;
	}

	public String toString() {
		return "ModPack{id='" + id + "', version='" + version + "', minecraftVersion='" + minecraftVersion + "', libraries=" + libraries + ", mods=" + mods + "}";
	}
}
