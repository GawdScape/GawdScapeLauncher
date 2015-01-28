package com.gawdscape.launcher.ui;

import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 *
 * @author Vinnie
 */
public class TransparentPanel extends JPanel {

	private Insets insets;

	public TransparentPanel() {
	}

	public TransparentPanel(LayoutManager mgr) {
		setLayout(mgr);
	}

	public boolean isOpaque() {
		return false;
	}

	public void setInsets(int top, int left, int bottom, int right) {
		insets = new Insets(top, left, bottom, right);
	}

	public Insets getInsets() {
		if (insets == null) {
			return super.getInsets();
		}
		return insets;
	}
}
