package com.gawdscape.launcher.ui;

import com.gawdscape.launcher.GawdScapeLauncher;
import javax.imageio.ImageIO;

/**
 *
 * @author Vinnie
 */
public class CobblestonePanel extends TexturedPanel {

    public CobblestonePanel() {
	setOpaque(true);
	try {
	    bgImage = ImageIO.read(
		    GawdScapeLauncher.class.getResource("images/cobblestone.png")
	    ).getScaledInstance(32, 32, 16);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
