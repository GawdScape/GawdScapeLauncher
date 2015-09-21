package com.gawdscape.launcher.ui;

import com.gawdscape.launcher.util.ColorCodes;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Map;

/**
 *
 * @author Vinnie
 */
public class HyperLinkLabel extends JLabel {

    public HyperLinkLabel() {
	super();
	setForeground(ColorCodes.Blue);
	setCursor(new Cursor(Cursor.HAND_CURSOR));
	Font font = getFont();
	Map attributes = font.getAttributes();
	attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
	setFont(font.deriveFont(attributes));
    }

    @Override
    public boolean isOpaque() {
	return false;
    }
}
