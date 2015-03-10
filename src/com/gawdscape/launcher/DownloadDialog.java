package com.gawdscape.launcher;

import com.gawdscape.launcher.download.DownloadManager;
import com.gawdscape.launcher.download.Updater;
import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.ImageUtils;
import com.gawdscape.launcher.util.Log;

/**
 *
 * @author Vinnie
 */
public class DownloadDialog extends javax.swing.JDialog {

	/**
	 * Creates new form DownloadDialog
	 *
	 * @param parent
	 * @param modal
	 */
	public DownloadDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		setLocationRelativeTo(parent);
	}

	public void changeTitle(String s) {
		title.setText(s);
	}

	public void setExtracting() {
		title.setText("Extracting natives...");
		source.setText("");
		progressBar.setIndeterminate(true);
		progressLabel.setText("");
		progress.setText("This may take a few moments...");
		destinationLabel.setText("Extract to:");
		destination.setText("");
		totalProgress.setString("");
	}

	public void setLaunching() {
		title.setText("Launching Mod Pack...");
		source.setText("Enjoy :)");
		progress.setText("Complete");
		destinationLabel.setText("Launching in:");
		destination.setText(GawdScapeLauncher.config.getGameDir());
	}

	public void setFile(String fileName, String host, String localPath) {
		fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		source.setText(fileName + " from " + host);
		if (localPath.length() >= 60) {
			localPath = "..." + localPath.substring(localPath.length() - 57, localPath.length());
		}
		destination.setText(localPath);
	}

	public void setProgress(int percent, int dlKB, int totalKB) {
		progressBar.setValue(percent);
		progress.setText((dlKB / 1024) + " KB of " + (totalKB / 1024) + " KB copied");
	}

	public void setTotalProgress(int current, int total) {
		int percent = current * 100 / total;
		totalProgress.setValue(percent);
		totalProgress.setString("File " + current + " of " + total + " - " + percent + "% complete");
	}

	public void setDisableMods() {
		title.setText("Disabling mods...");
		source.setText("");
		progressBar.setIndeterminate(true);
		progressLabel.setText("");
		progress.setText("This may take a moment...");
		destinationLabel.setText("Mod Folder:");
		destination.setText(GawdScapeLauncher.config.getGameDir());
		totalProgress.setString("");
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        downloadPanel = new com.gawdscape.launcher.ui.DirtPanel();
        title = new com.gawdscape.launcher.ui.TransparentLabel();
        source = new com.gawdscape.launcher.ui.TransparentLabel();
        progressLabel = new com.gawdscape.launcher.ui.TransparentLabel();
        destinationLabel = new com.gawdscape.launcher.ui.TransparentLabel();
        progress = new com.gawdscape.launcher.ui.TransparentLabel();
        destination = new com.gawdscape.launcher.ui.TransparentLabel();
        progressBar = new javax.swing.JProgressBar();
        totalProgress = new javax.swing.JProgressBar();
        cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mod Pack Download");
        setIconImage(ImageUtils.getFavIcon());

        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("Mod Pack Download");
        title.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        source.setText("Preparing for download...");

        progressLabel.setText("Progress:");
        progressLabel.setToolTipText("");

        destinationLabel.setText("Download to:");

        progress.setText("0 KB of 0 KB copied");
        progress.setToolTipText("");

        destination.setText(Directories.getWorkingDirectory().toString());

        progressBar.setForeground(new java.awt.Color(104, 223, 106));

        totalProgress.setForeground(new java.awt.Color(104, 223, 106));
        totalProgress.setString("File 0 of 0 - 0% complete");
        totalProgress.setStringPainted(true);

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout downloadPanelLayout = new javax.swing.GroupLayout(downloadPanel);
        downloadPanel.setLayout(downloadPanelLayout);
        downloadPanelLayout.setHorizontalGroup(
            downloadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(downloadPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(downloadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalProgress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, downloadPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cancel))
                    .addComponent(source, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(downloadPanelLayout.createSequentialGroup()
                        .addGroup(downloadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(progressLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(destinationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(downloadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(progress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(destination, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        downloadPanelLayout.setVerticalGroup(
            downloadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(downloadPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(source, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(downloadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(progressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(downloadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(destinationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(destination, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(totalProgress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(cancel)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(downloadPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(downloadPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
		cancel.setEnabled(false);
		DownloadManager.pool.shutdownNow();
		try {
			Updater.sleep(Long.MAX_VALUE);
		} catch (InterruptedException ex) {
			Log.error("Error stopping update thread.", ex);
		}
    }//GEN-LAST:event_cancelActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the System look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(LauncherFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				DownloadDialog dialog = new DownloadDialog(new javax.swing.JFrame(), true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private com.gawdscape.launcher.ui.TransparentLabel destination;
    private com.gawdscape.launcher.ui.TransparentLabel destinationLabel;
    private com.gawdscape.launcher.ui.DirtPanel downloadPanel;
    private com.gawdscape.launcher.ui.TransparentLabel progress;
    private javax.swing.JProgressBar progressBar;
    private com.gawdscape.launcher.ui.TransparentLabel progressLabel;
    private com.gawdscape.launcher.ui.TransparentLabel source;
    private com.gawdscape.launcher.ui.TransparentLabel title;
    private javax.swing.JProgressBar totalProgress;
    // End of variables declaration//GEN-END:variables
}
