package com.gawdscape.json.modpacks;

import com.gawdscape.json.game.ZipArchive;
import com.gawdscape.json.game.Library;
import com.gawdscape.json.game.Mod;
import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.OperatingSystem;

import java.io.File;
import java.util.*;

/**
 *
 * @author Vinnie
 */
public class ModPack {

    private String id;
    private String version;
    private String minecraftVersion;
    private String texperienceVersion;
    private String mainClass;
    private String minecraftArguments;
    private List<Library> libraries;
    private List<Mod> mods;
    private List<ZipArchive> archives;

    public ModPack() {
    }

    public ModPack(String id, String version, String minecraftVersion, String texperienceVersion, String mainClass, String minecraftArguments) {
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
	this.texperienceVersion = texperienceVersion;
	this.mainClass = mainClass;
	this.minecraftArguments = minecraftArguments;
	libraries = new ArrayList<>();
	mods = new ArrayList<>();
    }

    public ModPack(ModPack version) {
	this(version.getId(), version.getVersion(), version.getMinecraftVersion(), version.getTexperienceVersion(), version.getMainClass(), version.getMinecraftArguments());
	version.getLibraries().stream().forEach((lib) -> libraries.add(new Library(lib)));
	version.getMods().stream().forEach((mod) -> mods.add(new Mod(mod)));
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

    public String getTexperienceVersion() {
	return texperienceVersion;
    }

    public List<Library> getLibraries() {
	return libraries;
    }

    public Collection<Library> getRelevantLibraries() {
	List<Library> result = new ArrayList<>();
	if (libraries == null) {
	    return result;
	}
	libraries.stream().filter((library) -> (library.appliesToCurrentEnvironment())).forEach(result::add);
	return result;
    }

    public List<Mod> getMods() {
	return mods;
    }

    public List<ZipArchive> getArchives() {
	return archives;
    }

    public String getMainClass() {
	return mainClass;
    }

    public String getMinecraftArguments() {
	return minecraftArguments;
    }

    public Collection<File> getClassPath() {
	Collection<Library> libs = getRelevantLibraries();
	Collection<File> result = new ArrayList<>();
	libs.stream().filter((lib) -> (lib.getNatives() == null)).forEach((lib) -> result.add(new File(Directories.getLibraryPath(), lib.getArtifactPath())));
	return result;
    }

    public Collection<String> getExtractFiles(OperatingSystem os) {
	Collection<Library> libs = getRelevantLibraries();
	Collection<String> result = new ArrayList<>();
	libs.stream().forEach((lib) -> {
	    Map<OperatingSystem, String> natives = lib.getNatives();
	    if ((natives != null) && (natives.containsKey(os))) {
		result.add("libraries/" + lib.getArtifactPath(natives.get(os)));
	    }
	});
	return result;
    }

    public Set<String> getRequiredFiles(OperatingSystem os) {
	Set<String> neededFiles = new HashSet<>();
	getRelevantLibraries().stream().forEach((lib) -> {
	    if (lib.getNatives() != null) {
		String natives = lib.getNatives().get(os);
		if (natives != null) {
		    neededFiles.add("libraries/" + lib.getArtifactPath(natives));
		}
	    } else {
		neededFiles.add("libraries/" + lib.getArtifactPath());
	    }
	});
	return neededFiles;
    }

    @Override
    public String toString() {
	return "ModPack{id='" + id + "', version='" + version + "', minecraftVersion='" + minecraftVersion + "', libraries=" + libraries + ", mods=" + mods + "}";
    }
}
