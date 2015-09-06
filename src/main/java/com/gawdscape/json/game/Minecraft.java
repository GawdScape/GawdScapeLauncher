package com.gawdscape.json.game;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.OperatingSystem;

/**
 *
 * @author Vinnie
 */
public class Minecraft {

	public enum ReleaseType {

		SNAPSHOT, RELEASE, OLD_BETA, OLD_ALPHA
	}

	private String id;
	private Date time;
	private Date releaseTime;
	private ReleaseType type;
	private String minecraftArguments;
	private List<Library> libraries;
	private String mainClass;
	private int minimumLauncherVersion;
	private String incompatibilityReason;
	private String assets;
	private List<Rule> rules;
	private transient boolean synced = false;

	public Minecraft() {
	}

	public Minecraft(String id, Date releaseTime, Date updateTime, ReleaseType type, String mainClass, String minecraftArguments) {
		if ((id == null) || (id.length() == 0)) {
			throw new IllegalArgumentException("ID cannot be null or empty");
		}
		if (releaseTime == null) {
			throw new IllegalArgumentException("Release time cannot be null");
		}
		if (updateTime == null) {
			throw new IllegalArgumentException("Update time cannot be null");
		}
		if (type == null) {
			throw new IllegalArgumentException("Release type cannot be null");
		}
		if ((mainClass == null) || (mainClass.length() == 0)) {
			throw new IllegalArgumentException("Main class cannot be null or empty");
		}
		if (minecraftArguments == null) {
			throw new IllegalArgumentException("Process arguments cannot be null or empty");
		}
		this.id = id;
		this.releaseTime = releaseTime;
		this.time = updateTime;
		this.type = type;
		this.mainClass = mainClass;
		libraries = new ArrayList();
		this.minecraftArguments = minecraftArguments;
	}

	public Minecraft(Minecraft version) {
		this(version.getId(), version.getReleaseTime(), version.getUpdatedTime(), version.getType(), version.getMainClass(), version.getMinecraftArguments());
		version.getLibraries().stream().forEach((lib) -> libraries.add(new Library(lib)));
	}

	public Minecraft(Minecraft version, String mainClass, String minecraftArguments) {
		this(version.getId(), version.getReleaseTime(), version.getUpdatedTime(), version.getType(), mainClass, minecraftArguments);
	}

	public String getId() {
		return id;
	}

	public ReleaseType getType() {
		return type;
	}

	public Date getUpdatedTime() {
		return time;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public List<Library> getLibraries() {
		return libraries;
	}

	public String getMainClass() {
		return mainClass;
	}

	public void setUpdatedTime(Date time) {
		if (time == null) {
			throw new IllegalArgumentException("Time cannot be null");
		}
		this.time = time;
	}

	public void setReleaseTime(Date time) {
		if (time == null) {
			throw new IllegalArgumentException("Time cannot be null");
		}
		releaseTime = time;
	}

	public void setType(ReleaseType type) {
		if (type == null) {
			throw new IllegalArgumentException("Release type cannot be null");
		}
		this.type = type;
	}

	public void setMainClass(String mainClass) {
		if ((mainClass == null) || (mainClass.length() == 0)) {
			throw new IllegalArgumentException("Main class cannot be null or empty");
		}
		this.mainClass = mainClass;
	}

	public Collection<Library> getRelevantLibraries() {
		List<Library> result = new ArrayList();
		libraries.stream().filter((lib) -> (lib.appliesToCurrentEnvironment())).forEach(result::add);
		return result;
	}

	public Collection<File> getClassPath() {
		Collection<Library> libs = getRelevantLibraries();
		Collection<File> result = new ArrayList();
		libs.stream().filter((lib) -> (lib.getNatives() == null)).forEach((lib) -> result.add(new File(Directories.getLibraryPath(), lib.getArtifactPath())));
		return result;
	}

	public Collection<String> getExtractFiles(OperatingSystem os) {
		Collection<Library> libs = getRelevantLibraries();
		Collection<String> result = new ArrayList();
		libs.stream().forEach((lib) -> {
			Map<OperatingSystem, String> natives = lib.getNatives();
			if ((natives != null) && (natives.containsKey(os))) {
				result.add("libraries/" + lib.getArtifactPath((String) natives.get(os)));
			}
		});
		return result;
	}

	public Set<String> getRequiredFiles(OperatingSystem os) {
		Set<String> neededFiles = new HashSet();
		getRelevantLibraries().stream().forEach((lib) -> {
			if (lib.getNatives() != null) {
				String natives = (String) lib.getNatives().get(os);
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
		return "Minecraft{id='" + id + '\'' + ", time=" + time + ", type=" + type + ", libraries=" + libraries + ", mainClass='" + mainClass + '\'' + ", minimumLauncherVersion=" + minimumLauncherVersion + '}';
	}

	public String getMinecraftArguments() {
		return minecraftArguments;
	}

	public void setMinecraftArguments(String minecraftArguments) {
		if (minecraftArguments == null) {
			throw new IllegalArgumentException("Process arguments cannot be null or empty");
		}
		this.minecraftArguments = minecraftArguments;
	}

	public int getMinimumLauncherVersion() {
		return minimumLauncherVersion;
	}

	public void setMinimumLauncherVersion(int minimumLauncherVersion) {
		this.minimumLauncherVersion = minimumLauncherVersion;
	}

	public boolean appliesToCurrentEnvironment() {
		if (rules == null) {
			return true;
		}
		Rule.Action lastAction = Rule.Action.DISALLOW;
		for (Rule rule : rules) {
			Rule.Action action = rule.getAppliedAction();
			if (action != null) {
				lastAction = action;
			}
		}
		return lastAction == Rule.Action.ALLOW;
	}

	public void setIncompatibilityReason(String incompatibilityReason) {
		this.incompatibilityReason = incompatibilityReason;
	}

	public String getIncompatibilityReason() {
		return incompatibilityReason;
	}

	public boolean isSynced() {
		return synced;
	}

	public void setSynced(boolean synced) {
		this.synced = synced;
	}

	public String getAssets() {
		if (assets == null) {
			return "legacy";
		}
		return assets;
	}

	public void setAssets(String assets) {
		this.assets = assets;
	}
}
