package com.gawdscape.launcher.updater;

/**
 *
 * @author Vinnie
 */
public interface ProgressDelegate {

	public void progressCallback(RBCWrapper rbc, double progress);
}
