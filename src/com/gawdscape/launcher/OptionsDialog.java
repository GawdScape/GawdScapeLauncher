package com.gawdscape.launcher;

import com.gawdscape.launcher.util.Directories;
import com.gawdscape.launcher.util.ImageUtils;
import com.gawdscape.launcher.util.Log;
import com.gawdscape.launcher.util.OperatingSystem;
import java.lang.management.ManagementFactory;
import javax.swing.JFileChooser;

/**
 *
 * @author Vinnie
 */
public class OptionsDialog extends javax.swing.JDialog {

    private static long maxMemoryMB;

    /**
     * Creates new form OptionsDialog
     * @param parent
     * @param modal
     */
    public OptionsDialog(java.awt.Frame parent, boolean modal) {
	super(parent, modal);
	maxMemoryMB = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize() / 1048576L;
	initComponents();
	setLocationRelativeTo(parent);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        optionsPanel = new com.gawdscape.launcher.ui.DirtPanel();
        titleLabel = new com.gawdscape.launcher.ui.TransparentLabel();
        logButton = new com.gawdscape.launcher.ui.TransparentButton();
        updateButton = new com.gawdscape.launcher.ui.TransparentButton();
        cancelButton = new com.gawdscape.launcher.ui.TransparentButton();
        saveButton = new com.gawdscape.launcher.ui.TransparentButton();
        launcherSetingsPanel = new com.gawdscape.launcher.ui.TransparentPanel();
        newsCheckBox = new com.gawdscape.launcher.ui.TransparentCheckBox();
        skipLauncherCheckBox = new com.gawdscape.launcher.ui.TransparentCheckBox();
        logSetingsPanel = new com.gawdscape.launcher.ui.TransparentPanel();
        logCheckBox = new com.gawdscape.launcher.ui.TransparentCheckBox();
        closeLogCheckBox = new com.gawdscape.launcher.ui.TransparentCheckBox();
        styleLogCheckBox = new com.gawdscape.launcher.ui.TransparentCheckBox();
        gameSetingsPanel = new com.gawdscape.launcher.ui.TransparentPanel();
        gameDirLabel = new com.gawdscape.launcher.ui.TransparentLabel();
        browseButton = new com.gawdscape.launcher.ui.TransparentButton();
        gameDirButton = new com.gawdscape.launcher.ui.TransparentButton();
        gameDirField = new javax.swing.JTextField();
        memoryLabel = new com.gawdscape.launcher.ui.TransparentLabel();
        allocatedMemoryLabel = new com.gawdscape.launcher.ui.TransparentLabel();
        memorySlider = new com.gawdscape.launcher.ui.TransparentSlider();
        minMemoryLabel = new com.gawdscape.launcher.ui.TransparentLabel();
        maxMemoryLabel = new com.gawdscape.launcher.ui.TransparentLabel();
        joinServerCheckBox = new com.gawdscape.launcher.ui.TransparentCheckBox();
        serverIP = new javax.swing.JTextField();
        windowSizeCheckBox = new com.gawdscape.launcher.ui.TransparentCheckBox();
        windowWidth = new javax.swing.JTextField();
        x = new com.gawdscape.launcher.ui.TransparentLabel();
        windowHeight = new javax.swing.JTextField();
        fullscreenCheckBox = new com.gawdscape.launcher.ui.TransparentCheckBox();
        javaSettings = new com.gawdscape.launcher.ui.HyperLinkLabel();

        fileChooser.setCurrentDirectory(GawdScapeLauncher.config.getGameDirectory());
        fileChooser.setDialogTitle("Choose Game Directory");
        fileChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Launcher Options");
        setIconImage(ImageUtils.getFavIcon());

        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Launcher Options");
        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        logButton.setText("Open Log");
        logButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logButtonActionPerformed(evt);
            }
        });

        updateButton.setText("Force Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        launcherSetingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Launcher Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N

        newsCheckBox.setSelected(GawdScapeLauncher.config.getShowNews());
        newsCheckBox.setText("Show News");

        skipLauncherCheckBox.setSelected(GawdScapeLauncher.config.getSkipLauncher());
        skipLauncherCheckBox.setText("Skip Launcher, Open Minecraft");

        javax.swing.GroupLayout launcherSetingsPanelLayout = new javax.swing.GroupLayout(launcherSetingsPanel);
        launcherSetingsPanel.setLayout(launcherSetingsPanelLayout);
        launcherSetingsPanelLayout.setHorizontalGroup(
            launcherSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(launcherSetingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newsCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(skipLauncherCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addContainerGap())
        );
        launcherSetingsPanelLayout.setVerticalGroup(
            launcherSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(launcherSetingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(launcherSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newsCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(skipLauncherCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        logSetingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Log Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N

        logCheckBox.setSelected(GawdScapeLauncher.config.getShowLog());
        logCheckBox.setText("Show Log");

        closeLogCheckBox.setSelected(GawdScapeLauncher.config.getCloseLog());
        closeLogCheckBox.setText("Close Log with Game");

        styleLogCheckBox.setSelected(GawdScapeLauncher.config.getStyleLog());
        styleLogCheckBox.setText("Parse Colors and Links");

        javax.swing.GroupLayout logSetingsPanelLayout = new javax.swing.GroupLayout(logSetingsPanel);
        logSetingsPanel.setLayout(logSetingsPanelLayout);
        logSetingsPanelLayout.setHorizontalGroup(
            logSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logSetingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(logSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(logSetingsPanelLayout.createSequentialGroup()
                        .addComponent(logCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 180, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(closeLogCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 180, Short.MAX_VALUE))
                    .addComponent(styleLogCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 180, Short.MAX_VALUE))
                .addContainerGap())
        );
        logSetingsPanelLayout.setVerticalGroup(
            logSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logSetingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(logSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(logCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(closeLogCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(styleLogCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gameSetingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Game Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N

        gameDirLabel.setText("Game Directory:");

        browseButton.setText("Browse");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        gameDirButton.setText("Open Game Dir");
        gameDirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameDirButtonActionPerformed(evt);
            }
        });

        gameDirField.setText(GawdScapeLauncher.config.getGameDir());

        memoryLabel.setText("Memory to Allocate:");

        allocatedMemoryLabel.setText(GawdScapeLauncher.config.getMemory() + " MB");

        memorySlider.setMajorTickSpacing(1024);
        memorySlider.setMaximum((int) maxMemoryMB);
        memorySlider.setMinimum(512);
        memorySlider.setMinorTickSpacing(256);
        memorySlider.setPaintTicks(true);
        memorySlider.setSnapToTicks(true);
        memorySlider.setValue(GawdScapeLauncher.config.getMemory());
        memorySlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                memorySliderStateChanged(evt);
            }
        });

        minMemoryLabel.setText("512 MB");

        maxMemoryLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        maxMemoryLabel.setText((maxMemoryMB) + " MB");

        joinServerCheckBox.setSelected(GawdScapeLauncher.config.getjoinServer());
        joinServerCheckBox.setText("Join Server on Launch");
        joinServerCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                joinServerCheckBoxItemStateChanged(evt);
            }
        });

        serverIP.setText(GawdScapeLauncher.config.getServerIP());
        serverIP.setEnabled(GawdScapeLauncher.config.getjoinServer());

        windowSizeCheckBox.setSelected(GawdScapeLauncher.config.getWindowSize());
        windowSizeCheckBox.setText("Set Minecraft Window Size");
        windowSizeCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                windowSizeCheckBoxItemStateChanged(evt);
            }
        });

        windowWidth.setText(GawdScapeLauncher.config.getWindowWidth());
        windowWidth.setEnabled(GawdScapeLauncher.config.getWindowSize());

        x.setText("x");

        windowHeight.setText(GawdScapeLauncher.config.getWindowHeight());
        windowHeight.setEnabled(GawdScapeLauncher.config.getWindowSize());

        fullscreenCheckBox.setSelected(GawdScapeLauncher.config.getFullscreen());
        fullscreenCheckBox.setText("Launch in Fullscreen");

        javaSettings.setText("Java Settings");
        javaSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                javaSettingsMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout gameSetingsPanelLayout = new javax.swing.GroupLayout(gameSetingsPanel);
        gameSetingsPanel.setLayout(gameSetingsPanelLayout);
        gameSetingsPanelLayout.setHorizontalGroup(
            gameSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameSetingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gameSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gameSetingsPanelLayout.createSequentialGroup()
                        .addComponent(gameDirLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(browseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(gameDirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(gameSetingsPanelLayout.createSequentialGroup()
                        .addComponent(memoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(allocatedMemoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(memorySlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(gameDirField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(gameSetingsPanelLayout.createSequentialGroup()
                        .addComponent(minMemoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(maxMemoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(gameSetingsPanelLayout.createSequentialGroup()
                        .addGroup(gameSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(joinServerCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(windowSizeCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                            .addComponent(fullscreenCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(gameSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(gameSetingsPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(gameSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(gameSetingsPanelLayout.createSequentialGroup()
                                        .addComponent(windowWidth, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(x, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(windowHeight, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))
                                    .addComponent(serverIP)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gameSetingsPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(javaSettings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        gameSetingsPanelLayout.setVerticalGroup(
            gameSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameSetingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gameSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gameDirLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gameDirButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gameDirField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(gameSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(memoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(allocatedMemoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(memorySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gameSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(minMemoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maxMemoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(gameSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(joinServerCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serverIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(gameSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(windowSizeCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(windowWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(x, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(windowHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(gameSetingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fullscreenCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(javaSettings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout optionsPanelLayout = new javax.swing.GroupLayout(optionsPanel);
        optionsPanel.setLayout(optionsPanelLayout);
        optionsPanelLayout.setHorizontalGroup(
            optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(launcherSetingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logSetingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(gameSetingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optionsPanelLayout.createSequentialGroup()
                        .addComponent(logButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        optionsPanelLayout.setVerticalGroup(
            optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionsPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(launcherSetingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(logSetingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(gameSetingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(optionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(optionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
	GawdScapeLauncher.config.setConfig(
		gameDirField.getText(),
		memorySlider.getValue(),
		logCheckBox.isSelected(),
		newsCheckBox.isSelected(),
		closeLogCheckBox.isSelected(),
		skipLauncherCheckBox.isSelected(),
		joinServerCheckBox.isSelected(),
		windowSizeCheckBox.isSelected(),
		fullscreenCheckBox.isSelected(),
                styleLogCheckBox.isSelected(),
		serverIP.getText(),
		windowWidth.getText(),
		windowHeight.getText()
	);
	Config.saveConfig(GawdScapeLauncher.config);
	Directories.createGameDirs(GawdScapeLauncher.config.getGameDirectory());
	dispose();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
	dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void gameDirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameDirButtonActionPerformed
	OperatingSystem.openFolder(GawdScapeLauncher.config.getGameDirectory());
    }//GEN-LAST:event_gameDirButtonActionPerformed

    private void logButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logButtonActionPerformed
	if (GawdScapeLauncher.logFrame == null) {
	    GawdScapeLauncher.logFrame = new LogFrame(styleLogCheckBox.isSelected());
            Log.showLog = true;
	}
	GawdScapeLauncher.logFrame.setVisible(true);
        GawdScapeLauncher.logFrame.requestFocus();
    }//GEN-LAST:event_logButtonActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
	updateButton.setEnabled(false);
	updateButton.setText("Forcing Update");
	Log.info("Will update game on launch.");
	Config.forceUpdate = true;
    }//GEN-LAST:event_updateButtonActionPerformed

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
	int returnVal = fileChooser.showOpenDialog(this);

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    gameDirField.setText(fileChooser.getSelectedFile().toString());
	}
    }//GEN-LAST:event_browseButtonActionPerformed

    private void memorySliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_memorySliderStateChanged
	allocatedMemoryLabel.setText(memorySlider.getValue() + " MB");
    }//GEN-LAST:event_memorySliderStateChanged

    private void joinServerCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_joinServerCheckBoxItemStateChanged
	serverIP.setEnabled(joinServerCheckBox.isSelected());
    }//GEN-LAST:event_joinServerCheckBoxItemStateChanged

    private void windowSizeCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_windowSizeCheckBoxItemStateChanged
	windowWidth.setEnabled(windowSizeCheckBox.isSelected());
	windowHeight.setEnabled(windowSizeCheckBox.isSelected());
    }//GEN-LAST:event_windowSizeCheckBoxItemStateChanged

    private void javaSettingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_javaSettingsMouseClicked
	JavaSettingsDialog dialog = new JavaSettingsDialog((javax.swing.JFrame) getParent(), true);
	dialog.setVisible(true);
    }//GEN-LAST:event_javaSettingsMouseClicked

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
		OptionsDialog dialog = new OptionsDialog(new javax.swing.JFrame(), true);
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
    private com.gawdscape.launcher.ui.TransparentLabel allocatedMemoryLabel;
    private com.gawdscape.launcher.ui.TransparentButton browseButton;
    private com.gawdscape.launcher.ui.TransparentButton cancelButton;
    private com.gawdscape.launcher.ui.TransparentCheckBox closeLogCheckBox;
    private javax.swing.JFileChooser fileChooser;
    private com.gawdscape.launcher.ui.TransparentCheckBox fullscreenCheckBox;
    private com.gawdscape.launcher.ui.TransparentButton gameDirButton;
    private javax.swing.JTextField gameDirField;
    private com.gawdscape.launcher.ui.TransparentLabel gameDirLabel;
    private com.gawdscape.launcher.ui.TransparentPanel gameSetingsPanel;
    private com.gawdscape.launcher.ui.HyperLinkLabel javaSettings;
    private com.gawdscape.launcher.ui.TransparentCheckBox joinServerCheckBox;
    private com.gawdscape.launcher.ui.TransparentPanel launcherSetingsPanel;
    private com.gawdscape.launcher.ui.TransparentButton logButton;
    private com.gawdscape.launcher.ui.TransparentCheckBox logCheckBox;
    private com.gawdscape.launcher.ui.TransparentPanel logSetingsPanel;
    private com.gawdscape.launcher.ui.TransparentLabel maxMemoryLabel;
    private com.gawdscape.launcher.ui.TransparentLabel memoryLabel;
    private com.gawdscape.launcher.ui.TransparentSlider memorySlider;
    private com.gawdscape.launcher.ui.TransparentLabel minMemoryLabel;
    private com.gawdscape.launcher.ui.TransparentCheckBox newsCheckBox;
    private com.gawdscape.launcher.ui.DirtPanel optionsPanel;
    private com.gawdscape.launcher.ui.TransparentButton saveButton;
    private javax.swing.JTextField serverIP;
    private com.gawdscape.launcher.ui.TransparentCheckBox skipLauncherCheckBox;
    private com.gawdscape.launcher.ui.TransparentCheckBox styleLogCheckBox;
    private com.gawdscape.launcher.ui.TransparentLabel titleLabel;
    private com.gawdscape.launcher.ui.TransparentButton updateButton;
    private javax.swing.JTextField windowHeight;
    private com.gawdscape.launcher.ui.TransparentCheckBox windowSizeCheckBox;
    private javax.swing.JTextField windowWidth;
    private com.gawdscape.launcher.ui.TransparentLabel x;
    // End of variables declaration//GEN-END:variables
}
