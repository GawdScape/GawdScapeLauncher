package com.gawdscape.launcher.util;

import com.gawdscape.launcher.GawdScapeLauncher;

import java.util.logging.Level;

/**
 *
 * @author Vinnie
 */
public class Utils {

    public static void benchmark(String text, long start) {
	long end = System.currentTimeMillis();
	GawdScapeLauncher.LOGGER.log(Level.FINE, text, end - start);
    }
}
