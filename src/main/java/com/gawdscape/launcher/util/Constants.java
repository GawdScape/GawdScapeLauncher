package com.gawdscape.launcher.util;

import com.gawdscape.json.auth.Agent;
import com.gawdscape.launcher.GawdScapeLauncher;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;

/**
 *
 * @author Vinnie
 */
public class Constants {

    public static final int VERSION = 16;

    public static final int THIS_YEAR = 2015;

    public static final Agent MINECRAFT = new Agent("Minecraft", 1);

    public static final URL MC_LOGIN = constantURL("https://authserver.mojang.com/authenticate");
    public static final URL MC_REFRESH = constantURL("https://authserver.mojang.com/refresh");
    public static final URL LAUNCHER_NEWS = constantURL("http://launcher.gawdscape.com/news");
    public static final String MC_DOWNLOAD_URL = "https://s3.amazonaws.com/Minecraft.Download/";
    public static final String MC_LIBRARY_URL = "https://libraries.minecraft.net/";
    public static final String MC_ASSET_URL = "http://resources.download.minecraft.net/";
    public static final String GS_PACK_URL = "https://raw.githubusercontent.com/GawdScape/GawdPack/";
    public static final String GS_STORAGE_URL = "https://raw.githubusercontent.com/GawdScape/GawdStorage/";
    public static final String TXT_MOD_URL = "https://github.com/GawdScape/TexperienceMod/releases/download/";
    public static final String LAUNCHER_VERSION_URL = "https://raw.githubusercontent.com/GawdScape/GawdScapeLauncher/master/latest.version";
    public static final URI UPDATE_LINK = constantURI("https://github.com/GawdScape/GawdScapeLauncher/releases/latest");
    public static final URI GS_FORUM_LINK = constantURI("http://www.gawdscape.com/");
    public static final URI GS_GITHUB_LINK = constantURI("https://github.com/GawdScape");
    public static final URI LAUNCHER_WIKI_LINK = constantURI("https://github.com/GawdScape/GawdScapeLauncher/wiki");
    public static final URI LAUNCHER_ISSUE_LINK = constantURI("https://github.com/GawdScape/GawdScapeLauncher/issues");
    public static final URI MC_BUY_LINK = constantURI("https://minecraft.net/store/minecraft");

    public static final String GS_SERVER_IP = "mc.gawdscape.com";
    public static final int MC_AUTH_TIMEOUT = 15000;

    public static URI constantURI(String input) {
	try {
	    return new URI(input);
	} catch (URISyntaxException e) {
	    GawdScapeLauncher.logger.log(Level.SEVERE, "URI Error", e);
	    return null;
	}
    }

    private static URL constantURL(String input) {
	try {
	    return new URL(input);
	} catch (MalformedURLException e) {
	    GawdScapeLauncher.logger.log(Level.SEVERE, "URL Error", e);
	    return null;
	}
    }

    public static String getMcJar(String version) {
	return MC_DOWNLOAD_URL + "versions/" + version + "/" + version + ".jar";
    }

    public static String getMcJson(String version) {
	return MC_DOWNLOAD_URL + "versions/" + version + "/" + version + ".json";
    }

    public static String getTexperienceJar(String version, String mcVersion) {
        return TXT_MOD_URL + version + "/" + mcVersion + ".jar";
    }
}
