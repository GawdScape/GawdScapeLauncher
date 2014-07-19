package com.gawdscape.launcher.util;

import com.gawdscape.launcher.auth.Agent;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author Vinnie
 */
public class Constants {

    public static final int VERSION = 1;

    public static final Agent MINECRAFT = new Agent("Minecraft", 1);

    public static final URL MC_AUTH = constantURL("https://authserver.mojang.com/authenticate");
    public static final URL MC_REFRESH = constantURL("https://authserver.mojang.com/refresh");
    public static final URL LAUNCHER_NEWS = constantURL("http://launcher.gawdscape.com/news.php");
    public static final String MC_DOWNLOAD_URL = "https://s3.amazonaws.com/Minecraft.Download/";
    public static final String MC_LIBRARY_URL = "https://libraries.minecraft.net/";
    public static final String MC_ASSET_URL = "http://resources.download.minecraft.net/";
    public static final String GS_DOWNLOAD_URL = "http://launcher.gawdscape.com/files/";
    public static final String GS_LIBRARY_URL = "http://launcher.gawdscape.com/files/libraries/";
    public static final URI HELP_LINK = constantURI("http://launcher.gawdscape.com/help.php");
    public static final URI REGISTER_LINK = constantURI("https://account.mojang.com/register");
    public static final URI UPDATE_LINK = constantURI("http://launcher.gawdscape.com/update.php");

    public static URI constantURI(String input) {
	try {
	    return new URI(input);
	} catch (URISyntaxException e) {
	    Log.error("URI Error", e);
	    return null;
	}
    }

    private static URL constantURL(String input) {
	try {
	    return new URL(input);
	} catch (MalformedURLException e) {
	    Log.error("URL Error", e);
	    return null;
	}
    }
}
