package com.gawdscape.launcher.ui;

import java.awt.Color;
import javax.swing.JLabel;

/**
 *
 * @author Vinnie
 */
public class TransparentLabel extends JLabel {

    public TransparentLabel(String text, int horizontalAlignment) {
	super(text, horizontalAlignment);
	setForeground(Color.WHITE);
    }

    public TransparentLabel() {
	super();
	setForeground(Color.WHITE);
    }

    public boolean isOpaque() {
	return false;
    }
}
