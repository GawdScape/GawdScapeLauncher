package com.gawdscape.launcher.ui;

import javax.swing.JButton;

/**
 *
 * @author Vinnie
 */
public class TransparentButton extends JButton {

	public TransparentButton() {
		super();
	}

	@Override
	public boolean isOpaque() {
		return false;
	}
}
