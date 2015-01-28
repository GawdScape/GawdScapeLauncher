package com.gawdscape.launcher.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import javax.swing.JPanel;

/**
 *
 * @author Vinnie
 */
public class TexturedPanel extends JPanel {

	public Image img;
	public Image bgImage;

	public TexturedPanel() {
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	@Override
	public void paintComponent(Graphics g) {
		int w = getWidth() / 2 + 1;
		int h = getHeight() / 2 + 1;
		if ((img == null)
				|| (img.getWidth(null) != w)
				|| (img.getHeight(null) != h)) {
			img = createImage(w, h);

			Graphics graphics = img.getGraphics();
			int y;
			for (int x = 0; x <= w / 32; x++) {
				for (y = 0; y <= h / 32; y++) {
					graphics.drawImage(bgImage, x * 32, y * 32, null);
				}
			}
			if ((graphics instanceof Graphics2D)) {
				Graphics2D graphics2D = (Graphics2D) graphics;
				y = 1;
				graphics2D.setPaint(
						new GradientPaint(
								new Point2D.Float(0.0F, 0.0F),
								new Color(553648127, true),
								new Point2D.Float(0.0F, y),
								new Color(0, true)
						)
				);
				graphics2D.fillRect(0, 0, w, y);

				y = h;
				graphics2D.setPaint(
						new GradientPaint(
								new Point2D.Float(0.0F, 0.0F),
								new Color(0, true),
								new Point2D.Float(0.0F, y),
								new Color(1610612736, true)
						)
				);
				graphics2D.fillRect(0, 0, w, y);
			}
			graphics.dispose();
		}
		g.drawImage(img, 0, 0, w * 2, h * 2, null);
	}
}
