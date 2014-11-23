package com.gawdscape.launcher.util;

import com.gawdscape.launcher.GawdScapeLauncher;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 *
 * @author Vinnie
 */
public class Log {

    public static boolean showLog;
    public static boolean formatLog;

    public static void println(String line) {
        System.out.println(line);
        if (showLog) {
            if (formatLog) {
                GawdScapeLauncher.logFrame.formatAndPrint(line);
            } else {
                GawdScapeLauncher.logFrame.print(line + "\n", null);
            }
        }
    }

    public static void log(Level level, String text) {
        DateFormat df = new SimpleDateFormat("[HH:mm:ss]");
        Date date = new Date();
	println(df.format(date) + " [Launcher/" + level + "]: " + text);
    }

    public static void log(Level level, String text, Throwable t) {
	log(level, text + " " + t.getMessage());
        // Print our stack trace
        println(t.toString());
        StackTraceElement[] trace = t.getStackTrace();
        for (StackTraceElement traceElement : trace)
            println("\tat " + traceElement);
    }

    public static void info(String text) {
	log(Level.INFO, text);
    }

    public static void config(String text) {
	log(Level.CONFIG, text);
    }

    public static void fine(String text) {
	log(Level.FINE, text);
    }

    public static void finer(String text) {
	log(Level.FINER, text);
    }

    public static void finest(String text) {
	log(Level.FINEST, text);
    }

    public static void severe(String text) {
	log(Level.SEVERE, text);
    }

    public static void warning(String text) {
	log(Level.WARNING, text);
    }

    public static void error(String text, Throwable t) {
	log(Level.SEVERE, text, t);
    }

    public static void debug(String text) {
	log(Level.FINE, text);
    }
}
