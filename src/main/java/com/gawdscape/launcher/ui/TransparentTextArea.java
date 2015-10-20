package com.gawdscape.launcher.ui;

import javax.swing.*;
import java.awt.*;

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
