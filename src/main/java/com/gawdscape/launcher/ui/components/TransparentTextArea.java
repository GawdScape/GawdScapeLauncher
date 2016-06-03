package com.gawdscape.launcher.ui.components;

import java.awt.Color;
import javax.swing.JTextArea;

/**
 *
 * @author Vinnie
 */
public class TransparentTextArea extends JTextArea {

    public TransparentTextArea() {
	super();
	setForeground(Color.WHITE);
    }

    @Override
    public boolean isOpaque() {
	return false;
    }
}
