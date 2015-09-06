package com.gawdscape.launcher.updater;

/**
 *
 * @author Vinnie
 */
public interface ProgressDelegate {

	void progressCallback(RBCWrapper rbc, double progress);
}
