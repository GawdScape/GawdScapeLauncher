package com.gawdscape.launcher.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

/**
 *
 * @author Vinnie
 */
public enum OperatingSystem {

	LINUX("linux", new String[]{"linux", "unix"}), WINDOWS("windows", new String[]{"win"}), OSX("osx", new String[]{"mac"}), UNKNOWN("unknown", new String[0]);

	private final String name;
	private final String[] aliases;

	OperatingSystem(String name, String... aliases) {
		this.name = name;
		this.aliases = (aliases == null ? new String[0] : aliases);
	}

	public String getName() {
		return name;
	}

	public String[] getAliases() {
		return aliases;
	}

	public boolean isSupported() {
		return this != UNKNOWN;
	}

	public static String getArchDataModel() {
		return System.getProperty("sun.arch.data.model");
	}

	public static String getJavaDir() {
		String separator = System.getProperty("file.separator");
		String path = System.getProperty("java.home") + separator + "bin" + separator;
		if ((getCurrentPlatform() == WINDOWS)
				&& (new File(path + "javaw.exe").isFile())) {
			return path + "javaw.exe";
		}
		return path + "java";
	}

	public static OperatingSystem getCurrentPlatform() {
		String osName = System.getProperty("os.name").toLowerCase();
		for (OperatingSystem os : values()) {
			for (String alias : os.getAliases()) {
				if (osName.contains(alias)) {
					return os;
				}
			}
		}
		return UNKNOWN;
	}

	public static void openLink(URI link) {
		try {
			Class<?> desktopClass = Class.forName("java.awt.Desktop");
			Object desktop = desktopClass.getMethod("getDesktop", null).invoke(null);
			desktopClass.getMethod("browse", URI.class).invoke(desktop, link);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Log.error("Failed to open link " + link.toString(), e);
		}
	}

	public static void openFolder(File path) {
		String absolutePath = path.getAbsolutePath();
		OperatingSystem os = getCurrentPlatform();
		if (os == OSX) {
			try {
				Runtime.getRuntime().exec(new String[]{"/usr/bin/open", absolutePath});

				return;
			} catch (IOException e) {
				Log.error("Couldn't open " + path + " through /usr/bin/open", e);
			}
		} else if (os == WINDOWS) {
			String cmd = String.format("cmd.exe /C start \"Open file\" \"%s\"", absolutePath);
			try {
				Runtime.getRuntime().exec(cmd);
				return;
			} catch (IOException e) {
				Log.error("Couldn't open " + path + " through cmd.exe", e);
			}
		}
		try {
			Class<?> desktopClass = Class.forName("java.awt.Desktop");
			Object desktop = desktopClass.getMethod("getDesktop", null).invoke(null);
			desktopClass.getMethod("browse", URI.class).invoke(desktop, path.toURI());
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Log.error("Couldn't open " + path + " through Desktop.browse()", e);
		}
	}
}
