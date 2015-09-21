package com.gawdscape.launcher.util;

import com.gawdscape.launcher.GawdScapeLauncher;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * @author Vinnie
 */
public class LogHandler extends Handler {

    private final Formatter formatter = new LogFormatter();

    @Override
    public void publish(LogRecord record) {
	if (GawdScapeLauncher.logFrame != null) {
	    GawdScapeLauncher.logFrame.formatAndPrint(formatter.format(record));
	}
    }

    @Override
    public void flush() {
	// nothing
    }

    @Override
    public void close() throws SecurityException {
	// nothing
    }

}
