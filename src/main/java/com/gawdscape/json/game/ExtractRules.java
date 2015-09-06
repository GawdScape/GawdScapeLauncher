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
		exclude.stream().forEach(exclude::add);
	}

	public List<String> getExcludes() {
		return exclude;
	}

	public boolean shouldExtract(String path) {
		return exclude == null || exclude.stream().noneMatch((rule) -> (path.startsWith(rule)));
	}
}
