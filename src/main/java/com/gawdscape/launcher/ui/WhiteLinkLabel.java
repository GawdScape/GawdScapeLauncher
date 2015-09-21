package com.gawdscape.launcher.ui;

import javax.swing.*;
import java.awt.*;

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

    @Override
    public boolean isOpaque() {
	return false;
    }
}
