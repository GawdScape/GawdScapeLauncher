package com.gawdscape.launcher.updater;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/**
 *
 * @author Vinnie
 */
public class RBCWrapper implements ReadableByteChannel {

    private final ProgressDelegate delegate;
    private final long expectedSize;
    private final ReadableByteChannel rbc;
    private long readSoFar;

    public RBCWrapper(ReadableByteChannel rbc, long expectedSize, ProgressDelegate delegate) {
	this.delegate = delegate;
	this.expectedSize = expectedSize;
	this.rbc = rbc;
    }

    @Override
    public void close() throws IOException {
	rbc.close();
    }

    public long getExpectedSize() {
	return expectedSize;
    }

    public long getReadSoFar() {
	return readSoFar;
    }

    @Override
    public boolean isOpen() {
	return rbc.isOpen();
    }

    @Override
    public int read(ByteBuffer bb) throws IOException {
	int n;
	double progress;

	if ((n = rbc.read(bb)) > 0) {
	    readSoFar += n;
	    progress = expectedSize > 0 ? (double) readSoFar / (double) expectedSize * 100.0 : -1.0;
	    delegate.progressCallback(this, progress);
	}

	return n;
    }
}
