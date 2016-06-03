package com.gawdscape.launcher.ui.components;

import com.gawdscape.launcher.util.ColorCodes;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;
import javax.swing.JLabel;

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
