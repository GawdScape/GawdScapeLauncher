package com.gawdscape.launcher.game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gawdscape.launcher.util.OperatingSystem;

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

    public Library() {
    }

    public Library(String name) {
	if ((name == null) || (name.length() == 0)) {
	    throw new IllegalArgumentException("Library name cannot be null or empty");
	}
	this.name = name;
    }

    public Library(Library library) {
	name = name;
	url = url;
	if (extract != null) {
	    extract = new ExtractRules(extract);
	}
	if (rules != null) {
	    rules = new ArrayList();
	    for (Rule rule : rules) {
		rules.add(new Rule(rule));
	    }
	}
	if (natives != null) {
	    natives = new LinkedHashMap();
	    for (Map.Entry<OperatingSystem, String> entry : library.getNatives().entrySet()) {
		natives.put(entry.getKey(), entry.getValue());
	    }
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
	    natives = new EnumMap(OperatingSystem.class);
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
	String[] parts = name.split(":", 3);
	return String.format("%s-%s%s.jar", new Object[]{parts[1], parts[2], classifier});
    }

    public String toString() {
	return "Library{name='" + name + '\'' + ", rules=" + rules + ", natives=" + natives + ", extract=" + extract + '}';
    }
}
