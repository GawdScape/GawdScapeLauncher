package com.gawdscape.launcher.ui;

import java.awt.Color;
import javax.swing.JSlider;

/**
 *
 * @author Vinnie
 */
public class TransparentSlider extends JSlider {

    public TransparentSlider() {
	super();
	setForeground(Color.WHITE);
    }

    public boolean isOpaque() {
	return false;
    }
}
