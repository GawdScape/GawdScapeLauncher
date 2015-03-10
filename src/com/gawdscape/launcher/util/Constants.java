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

	public static final int VERSION = 11;

	public static final int THIS_YEAR = 2015;

	public static final Agent MINECRAFT = new Agent("Minecraft", 1);

	public static final URL MC_AUTH = constantURL("https://authserver.mojang.com/authenticate");
	public static final URL MC_REFRESH = constantURL("https://authserver.mojang.com/refresh");
	public static final URL LAUNCHER_NEWS = constantURL("http://launcher.gawdscape.com/news");
	public static final String MC_DOWNLOAD_URL = "https://s3.amazonaws.com/Minecraft.Download/";
	public static final String MC_LIBRARY_URL = "https://libraries.minecraft.net/";
	public static final String MC_ASSET_URL = "http://resources.download.minecraft.net/";
	public static final String GS_MOD_URL = "https://github.com/GawdScape/GawdMod/releases/download/";
	public static final String GS_PACK_URL = "https://raw.githubusercontent.com/GawdScape/GawdPack/";
	public static final String GS_PACK_FRONT_URL = "https://github.com/GawdScape/GawdPack/blob/";
	public static final String GS_STORAGE_URL = "https://raw.githubusercontent.com/GawdScape/GawdStorage/";
	public static final String LAUNCHER_VERSION_URL = "https://raw.githubusercontent.com/GawdScape/GawdScapeLauncher/master/latest.version";
	public static final URI UPDATE_LINK = constantURI("https://github.com/GawdScape/GawdScapeLauncher/releases/latest");
	public static final URI GS_FORUM_LINK = constantURI("http://www.gawdscape.com/");
	public static final URI GS_GITHUB_LINK = constantURI("https://github.com/GawdScape");
	public static final URI LAUNCHER_WIKI_LINK = constantURI("https://github.com/GawdScape/GawdScapeLauncher/wiki");
	public static final URI LAUNCHER_ISSUE_LINK = constantURI("https://github.com/GawdScape/GawdScapeLauncher/issues");
	public static final URI MC_BUY_LINK = constantURI("https://minecraft.net/store/minecraft");

	public static String GS_SERVER_IP = "server.gawdscape.com";

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
