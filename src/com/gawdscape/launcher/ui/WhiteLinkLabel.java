package com.gawdscape.launcher.ui;

import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JLabel;

/**
 *
 * @author Vinnie
 */
public class WhiteLinkLabel extends JLabel {

	public WhiteLinkLabel() {
		super();
		setForeground(Color.WHITE);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public boolean isOpaque() {
		return false;
	}
}
