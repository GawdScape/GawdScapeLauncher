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
public class GawdScape {

    private String id;
    private Date time;
    private String minecraftVersion;
    private String mainClass;
    private String minecraftArguments;
    private List<Library> libraries;
    private List<Mod> mods;

    public GawdScape() {
    }

    public GawdScape(String id, Date updateTime, String minecraftVersion) {
	if ((id == null) || (id.length() == 0)) {
	    throw new IllegalArgumentException("ID cannot be null or empty");
	}
	if (updateTime == null) {
	    throw new IllegalArgumentException("Update time cannot be null");
	}
	if ((minecraftVersion == null) || (minecraftVersion.length() == 0)) {
	    throw new IllegalArgumentException("Release type cannot be null");
	}
	this.id = id;
	this.time = updateTime;
	this.minecraftVersion = minecraftVersion;
	libraries = new ArrayList();
    }

    public GawdScape(GawdScape version) {
	this(version.getId(), version.getUpdatedTime(), version.getMinecraftVersion());
	for (Library library : version.getLibraries()) {
	    libraries.add(new Library(library));
	}
    }

    public GawdScape(GawdScape version, String mainClass, String minecraftArguments) {
	this(version.getId(), version.getUpdatedTime(), version.getMinecraftVersion());
    }

    public String getId() {
	return id;
    }

    public Date getUpdatedTime() {
	return time;
    }

    public String getMinecraftVersion() {
	return minecraftVersion;
    }

    public List<Library> getLibraries() {
	return libraries;
    }

    public void setUpdatedTime(Date time) {
	if (time == null) {
	    throw new IllegalArgumentException("Time cannot be null");
	}
	this.time = time;
    }

    public void setMinecraftVersion(String minecraftVersion) {
	if (minecraftVersion == null) {
	    throw new IllegalArgumentException("Release type cannot be null");
	}
	this.minecraftVersion = minecraftVersion;
    }

    public Collection<Library> getRelevantLibraries() {
	List<Library> result = new ArrayList();
	for (Library library : libraries) {
	    if (library.appliesToCurrentEnvironment()) {
		result.add(library);
	    }
	}
	return result;
    }

    public Collection<File> getClassPath() {
	Collection<Library> libraries = getRelevantLibraries();
	Collection<File> result = new ArrayList();
	for (Library library : libraries) {
	    if (library.getNatives() == null) {
		result.add(new File(Directories.getLibraryPath(), library.getArtifactPath()));
	    }
	}
	return result;
    }

    public Collection<String> getExtractFiles(OperatingSystem os) {
	Collection<Library> libraries = getRelevantLibraries();
	Collection<String> result = new ArrayList();
	for (Library library : libraries) {
	    Map<OperatingSystem, String> natives = library.getNatives();
	    if ((natives != null) && (natives.containsKey(os))) {
		result.add("libraries/" + library.getArtifactPath((String) natives.get(os)));
	    }
	}
	return result;
    }

    public Set<String> getRequiredFiles(OperatingSystem os) {
	Set<String> neededFiles = new HashSet();
	for (Library library : getRelevantLibraries()) {
	    if (library.getNatives() != null) {
		String natives = (String) library.getNatives().get(os);
		if (natives != null) {
		    neededFiles.add("libraries/" + library.getArtifactPath(natives));
		}
	    } else {
		neededFiles.add("libraries/" + library.getArtifactPath());
	    }
	}
	return neededFiles;
    }

    public String toString() {
	return "GawdScape{id='" + id + '\'' + ", time=" + time + ", minecraftVersion=" + minecraftVersion + ", libraries=" + libraries + '}';
    }
}
