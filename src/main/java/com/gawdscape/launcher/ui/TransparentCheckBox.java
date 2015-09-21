package com.gawdscape.launcher.ui;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Vinnie
 */
public class TransparentCheckBox extends JCheckBox {

    public TransparentCheckBox() {
	super();
	setForeground(Color.WHITE);
    }

    @Override
    public boolean isOpaque() {
	return false;
    }
}
