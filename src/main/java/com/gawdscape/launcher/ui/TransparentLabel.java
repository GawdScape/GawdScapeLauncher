package com.gawdscape.launcher.ui;

import javax.swing.*;
import java.awt.*;

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

    @Override
    public boolean isOpaque() {
	return false;
    }
}
