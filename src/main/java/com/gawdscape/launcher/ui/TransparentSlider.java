package com.gawdscape.launcher.ui;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Vinnie
 */
public class TransparentSlider extends JSlider {

    public TransparentSlider() {
	super();
	setForeground(Color.WHITE);
    }

    @Override
    public boolean isOpaque() {
	return false;
    }
}
