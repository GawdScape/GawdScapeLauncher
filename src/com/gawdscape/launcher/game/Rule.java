package com.gawdscape.launcher.game;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.gawdscape.launcher.util.OperatingSystem;

/**
 *
 * @author Vinnie
 */
public class Rule {

	public Rule() {
	}

	public static enum Action {

		ALLOW, DISALLOW;

		private Action() {
		}
	}

	public class OSRestriction {

		private OperatingSystem name;
		private String version;

		public OSRestriction() {
		}

		public OperatingSystem getName() {
			return name;
		}

		public String getVersion() {
			return version;
		}

		public OSRestriction(OSRestriction osRestriction) {
			name = name;
			version = version;
		}

		public boolean isCurrentOperatingSystem() {
			if ((name != null) && (name != OperatingSystem.getCurrentPlatform())) {
				return false;
			}
			if (version != null) {
				try {
					Pattern pattern = Pattern.compile(version);
					Matcher matcher = pattern.matcher(System.getProperty("os.version"));
					if (!matcher.matches()) {
						return false;
					}
				} catch (Throwable localThrowable) {
				}
			}
			return true;
		}

		public String toString() {
			return "OSRestriction{name=" + name + ", version='" + version + '\'' + '}';
		}
	}

	private Action action = Action.ALLOW;
	private OSRestriction os;

	public Rule(Rule rule) {
		action = action;
		if (os != null) {
			os = new OSRestriction(os);
		}
	}

	public Action getAppliedAction() {
		if ((os != null) && (!os.isCurrentOperatingSystem())) {
			return null;
		}
		return action;
	}

	public Action getAction() {
		return action;
	}

	public OSRestriction getOs() {
		return os;
	}

	public String toString() {
		return "Rule{action=" + action + ", os=" + os + '}';
	}
}
