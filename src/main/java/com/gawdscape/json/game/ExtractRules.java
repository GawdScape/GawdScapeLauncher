package com.gawdscape.json.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Vinnie
 */
public class ExtractRules {

	private final List<String> exclude;

	public ExtractRules() {
		this.exclude = new ArrayList();
	}

	public ExtractRules(String... exclude) {
		this.exclude = new ArrayList();
		if (exclude != null) {
			Collections.addAll(this.exclude, exclude);
		}
	}

	public ExtractRules(ExtractRules rules) {
		this.exclude = new ArrayList();
		exclude.stream().forEach((exclud) -> {
			exclude.add(exclud);
		});
	}

	public List<String> getExcludes() {
		return exclude;
	}

	public boolean shouldExtract(String path) {
		if (exclude == null) {
			return true;
		}
		return exclude.stream().noneMatch((rule) -> (path.startsWith(rule)));
	}
}
