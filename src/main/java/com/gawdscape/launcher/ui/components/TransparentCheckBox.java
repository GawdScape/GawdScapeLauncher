package com.gawdscape.launcher.ui.components;

import java.awt.Color;
import javax.swing.JCheckBox;

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
