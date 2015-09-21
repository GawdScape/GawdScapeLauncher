package com.gawdscape.launcher.util;

import com.gawdscape.launcher.GawdScapeLauncher;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 *
 * @author Vinnie
 */
public class ImageUtils {

    public static ImageIcon createImageIcon(String path) {
	URL imgURL = GawdScapeLauncher.class.getResource("images/" + path);
	if (imgURL != null) {
	    return new ImageIcon(imgURL);
	} else {
	    System.err.println("Couldn't find file: " + path);
	    return null;
	}
    }

    public static Image getFavIcon() {
	return createImageIcon("favicon.png").getImage();
    }

    public static ImageIcon getMissingPackLogo() {
	return createImageIcon("favicon.png");
    }
}
