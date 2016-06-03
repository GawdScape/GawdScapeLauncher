package com.gawdscape.launcher.util;

import com.gawdscape.launcher.GawdScapeLauncher;
import java.awt.Desktop;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;

/**
 *
 * @author Vinnie
 */
public enum OperatingSystem {

    LINUX("linux", "linux", "unix"), WINDOWS("windows", "win"), OSX("osx", "mac"), UNKNOWN("unknown");

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
        OperatingSystem os = getCurrentPlatform();
        Runtime rt = Runtime.getRuntime();
        if (null != os) switch (os) {
            case WINDOWS:
                try {
                    // this doesn't support showing urls in the form of "page.html#nameLink"
                    rt.exec( "rundll32 url.dll,FileProtocolHandler " + link.toString());
                    return;
                } catch (IOException ex) {
                    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Couldn't open " + link.toString() + " through url.dll", ex);
                }
                break;
            case OSX:
                try {
                    rt.exec( "open " + link.toString());
                    return;
                } catch (IOException ex) {
                    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Couldn't open " + link.toString() + " through /usr/bin/open", ex);
                }
                break;
            case LINUX:
                // Do a best guess on unix
                // List of "top 10" linux browsers
                String[] browsers = {"firefox", "chrome", "opera", "konqueror", "epiphany",
                    "midori", "qupzilla", "elinks", "links", "lynx"};
                
                // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
                StringBuilder cmd = new StringBuilder();
                for (int i=0; i < browsers.length; i++) {
                    cmd.append(i==0  ? "" : " || ").append(browsers[i]).append(" \"").append(link.toString()).append("\" ");
                }
                
                try {
                    rt.exec(new String[] { "sh", "-c", cmd.toString() });
                    return;
                } catch (IOException ex) {
                    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Couldn't open " + link.toString() + " through any known browser.", ex);
                    GawdScapeLauncher.LOGGER.info("Please report this error along with the name of your main web browser.");
                }
                break;
        }
	try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(link);
            }
	} catch (IOException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Failed to open link " + link.toString(), ex);
        } catch (UnsupportedOperationException ex) {
            GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Failed to open link " + link.toString(), ex);
            GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Your operating system does not support this feature.", ex);
	}
    }

    public static void openFolder(File path) {
	String absolutePath = path.getAbsolutePath();
	OperatingSystem os = getCurrentPlatform();
        if (os == WINDOWS) {
	    String cmd = String.format("cmd.exe /C start \"Open file\" \"%s\"", absolutePath);
	    try {
		Runtime.getRuntime().exec(cmd);
		return;
	    } catch (IOException ex) {
		GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Couldn't open " + path + " through cmd.exe", ex);
	    }
	} else if (os == OSX) {
	    try {
		Runtime.getRuntime().exec(new String[]{"/usr/bin/open", absolutePath});
		return;
	    } catch (IOException ex) {
		GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Couldn't open " + path + " through /usr/bin/open", ex);
	    }
        }
	try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(path.getAbsoluteFile());
            }
	} catch (IOException ex) {
	    GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Failed to open folder " + path, ex);
	}
    }

    public static int getProcessorCores() {
	return Runtime.getRuntime().availableProcessors();
    }
}
