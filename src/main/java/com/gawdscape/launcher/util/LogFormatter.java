package com.gawdscape.launcher.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author Vinnie
 */
public class LogFormatter extends Formatter {

    private final SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");

    @Override
    public String format(LogRecord record) {
	StringBuilder builder = new StringBuilder();

	builder.append("[");
	builder.append(date.format(record.getMillis()));
	builder.append("] [");
	builder.append(record.getLoggerName());
	builder.append("/");
	builder.append(record.getLevel());
	builder.append("]: ");
	builder.append(formatMessage(record));
	builder.append("\n");

	Throwable throwable = record.getThrown();
	if (throwable != null) {
	    StringWriter writer = new StringWriter();
	    throwable.printStackTrace(new PrintWriter(writer));
	    builder.append(writer);
	}

	return builder.toString();
    }
}
