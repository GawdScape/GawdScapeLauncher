package com.gawdscape.launcher.ui.components;

import com.gawdscape.launcher.GawdScapeLauncher;

import javax.imageio.ImageIO;

/**
 *
 * @author Vinnie
 */
public class DirtPanel extends TexturedPanel {

    public DirtPanel() {
	setOpaque(true);
	try {
	    bgImage = ImageIO.read(
		    GawdScapeLauncher.class.getResource("images/dirt.png")
	    ).getScaledInstance(32, 32, 16);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
