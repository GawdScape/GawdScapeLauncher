package com.gawdscape.launcher.download;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/**
 *
 * @author Vinnie
 */
public class RBCWrapper implements ReadableByteChannel {

    private RBCWrapperDelegate delegate;
    private long expectedSize;
    private ReadableByteChannel rbc;
    private long readSoFar;

    RBCWrapper(ReadableByteChannel rbc, long expectedSize, RBCWrapperDelegate delegate) {
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
	    delegate.rbcProgressCallback(this, progress);
	}

	return n;
    }
}
