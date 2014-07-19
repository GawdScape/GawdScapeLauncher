package com.gawdscape.launcher.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Vinnie
 */
public class ExtractRules {

    private List<String> exclude = new ArrayList();

    public ExtractRules() {
    }

    public ExtractRules(String... exclude) {
	if (exclude != null) {
	    Collections.addAll(this.exclude, exclude);
	}
    }

    public ExtractRules(ExtractRules rules) {
	for (String exclud : exclude) {
	    exclude.add(exclud);
	}
    }

    public List<String> getExcludes() {
	return exclude;
    }

    public boolean shouldExtract(String path) {
	if (exclude != null) {
	    for (String rule : exclude) {
		if (path.startsWith(rule)) {
		    return false;
		}
	    }
	}
	return true;
    }
}
