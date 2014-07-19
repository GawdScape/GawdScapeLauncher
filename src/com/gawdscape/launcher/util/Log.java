package com.gawdscape.launcher.util;

import com.gawdscape.launcher.GawdScapeLauncher;
import com.gawdscape.launcher.LogFrame;
import java.util.logging.Level;

/**
 *
 * @author Vinnie
 */
public class Log extends Thread {

    public void run() {
	GawdScapeLauncher.logFrame = new LogFrame();
	GawdScapeLauncher.logFrame.setVisible(true);
    }

    public static void print(String text) {
	System.out.println(text);
	printToLogFrame(text);
    }

    public static void printToLogFrame(final String text) {
	java.awt.EventQueue.invokeLater(new Runnable() {
	    public void run() {
		if (GawdScapeLauncher.logFrame != null) {
		    try {
			LogFrame.log.insertString(LogFrame.log.getLength(), text + "\n", null);
			GawdScapeLauncher.logFrame.logPane.setCaretPosition(LogFrame.log.getLength());
		    } catch (Exception ex) {
			ex.printStackTrace();
		    }
		}
	    }
	});
    }

    public static void log(Level level, String text) {
	System.out.println(level + ": " + text);
	printToLogFrame(level + ": " + text);
    }

    public static void log(Level level, String text, Throwable t) {
	System.out.println(level + ": " + text);
	t.printStackTrace();
	printToLogFrame(level + ": " + text + "\n" + t.getMessage());
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
