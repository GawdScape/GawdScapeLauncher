package com.gawdscape.json.game;

import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.OperatingSystem;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Vinnie
 */
public class Library {

    private String name;
    private List<Rule> rules;
    private Map<OperatingSystem, String> natives;
    private ExtractRules extract;
    private String url;
    private String classifier;

    public Library() {
    }

    public Library(String name) {
	if ((name == null) || (name.length() == 0)) {
	    throw new IllegalArgumentException("Library name cannot be null or empty");
	}
	this.name = name;
    }

    public Library(Library library) {
	name = library.name;
	url = library.url;
	if (library.extract != null) {
	    extract = new ExtractRules(library.extract);
	}
	if (library.rules != null) {
	    rules = new ArrayList<>();
	    library.rules.stream().forEach((rule) -> rules.add(new Rule(rule)));
	}
	if (library.natives != null) {
	    natives = new LinkedHashMap<>();
	    library.getNatives().entrySet().stream().forEach((entry) -> natives.put(entry.getKey(), entry.getValue()));
	}
    }

    public String getName() {
	return name;
    }

    public Library addNative(OperatingSystem operatingSystem, String name) {
	if ((operatingSystem == null) || (!operatingSystem.isSupported())) {
	    throw new IllegalArgumentException("Cannot add native for unsupported OS");
	}
	if ((name == null) || (name.length() == 0)) {
	    throw new IllegalArgumentException("Cannot add native for null or empty name");
	}
	if (natives == null) {
	    natives = new EnumMap<>(OperatingSystem.class);
	}
	natives.put(operatingSystem, name);
	return this;
    }

    public List<Rule> getRules() {
	return rules;
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

    public Map<OperatingSystem, String> getNatives() {
	return natives;
    }

    public ExtractRules getExtractRules() {
	return extract;
    }

    public Library setExtractRules(ExtractRules rules) {
	extract = rules;
	return this;
    }

    public String getArtifactBaseDir() {
	if (name == null) {
	    throw new IllegalStateException("Cannot get artifact dir of empty/blank artifact");
	}
	String[] parts = name.split(":", 3);
	return String.format("%s/%s/%s", parts[0].replaceAll("\\.", "/"), parts[1], parts[2]);
    }

    public String getArtifactPath() {
	return getArtifactPath(classifier);
    }

    public String getArtifactPath(String classifier) {
	if (name == null) {
	    throw new IllegalStateException("Cannot get artifact path of empty/blank artifact");
	}
	return String.format("%s/%s", getArtifactBaseDir(), getArtifactFilename(classifier));
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
	String[] parts = name.split(":", 3);
	return String.format("%s-%s%s.jar", parts[1], parts[2], classifier);
    }

    @Override
    public String toString() {
	return "Library{name='" + name + "'" + ", rules=" + rules + ", natives=" + natives + ", extract=" + extract + "}";
    }

    public boolean hasCustomUrl() {
	return url != null;
    }

    public String getDownloadUrl() {
	if (url != null) {
	    return url;
	}
	return Constants.MC_LIBRARY_URL;
    }
}
