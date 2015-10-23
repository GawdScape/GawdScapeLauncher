package com.gawdscape.launcher;

import com.gawdscape.json.game.Library;
import com.gawdscape.json.game.Mod;
import com.gawdscape.json.game.ZipArchive;
import com.gawdscape.json.modpacks.ModPack;
import com.gawdscape.launcher.util.FileUtils;
import com.google.gson.JsonSyntaxException;
import java.awt.Frame;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;

/**
 *
 * @author vinnie
 */
public class AboutModPackDialog extends javax.swing.JDialog {

    private final String id;
    private final String url;

    /**
     * Creates new form AboutModPackDialog
     */
    public AboutModPackDialog(Frame parent, ModPack modpack, String url) {
        super(parent, true);
        initComponents();
        this.id = modpack.getId();
        this.url = url;
        loadPackData(modpack);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void loadPackData(ModPack modpack) {
        try {
            packName.setText(modpack.getId());
            version.setText(modpack.getVersion());
            mcVersion.setText(modpack.getMinecraftVersion());
            if (modpack.getMainClass() != null)
                mainClass.setText(modpack.getMainClass());
            if (modpack.getMinecraftArguments() != null)
                arguments.setText(modpack.getMinecraftArguments());
            if (modpack.getTexperienceVersion() != null)
                txtVersion.setText(modpack.getTexperienceVersion());
            listLibraries(modpack.getLibraries());
            listMods(modpack.getMods());
            listArchives(modpack.getArchives());
            serverList.setText(String.valueOf(FileUtils.existsOnInternet(url + "/servers.dat")));
        } catch (JsonSyntaxException ex) {
            GawdScapeLauncher.logger.log(Level.SEVERE, "Error", ex);
        }
    }

    private void listLibraries(List<Library> libList) {
	if (libList == null) {
	    libraries.setText("No libraries.");
            return;
	}
        libList.stream().forEach((lib) -> {
            libraries.append((lib.getNatives() != null ? "[Native] " : "") + lib.getArtifactFilename(null) + " (" + lib.getDownloadUrl() + ")\n");
        });
    }

    private void listMods(List<Mod> modList) {
	if (modList == null) {
	    mods.setText("No mods.");
            return;
	}
        modList.stream().forEach((mod) -> {
            mods.append("[" + mod.getType() + "] " + mod.getArtifactFilename(null) + " (" + mod.getDownloadUrl() + ")\n");
        });
    }

    private void listArchives(List<ZipArchive> archiveList) {
	if (archiveList == null) {
	    archives.setText("No archives.");
            return;
	}
        archiveList.stream().forEach((archive) -> {
            archives.append(archive.getArtifactFilename(null) + " (" + archive.getDownloadUrl() + ")\n");
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        packName = new javax.swing.JLabel();
        javax.swing.JLabel versionLabel = new javax.swing.JLabel();
        javax.swing.JLabel minecraftLabel = new javax.swing.JLabel();
        javax.swing.JLabel txtLabel = new javax.swing.JLabel();
        mainClassLabel = new javax.swing.JLabel();
        javax.swing.JLabel mcArgsLabel = new javax.swing.JLabel();
        javax.swing.JLabel libsLabel = new javax.swing.JLabel();
        javax.swing.JLabel modsLabel = new javax.swing.JLabel();
        javax.swing.JLabel zipsLabel = new javax.swing.JLabel();
        javax.swing.JLabel serverLabel = new javax.swing.JLabel();
        javax.swing.JButton denyButton = new javax.swing.JButton();
        javax.swing.JButton acceptButton = new javax.swing.JButton();
        version = new javax.swing.JLabel();
        mcVersion = new javax.swing.JLabel();
        txtVersion = new javax.swing.JLabel();
        javax.swing.JScrollPane argumentsScrollPane = new javax.swing.JScrollPane();
        arguments = new javax.swing.JTextArea();
        javax.swing.JScrollPane librariesScrollPane = new javax.swing.JScrollPane();
        libraries = new javax.swing.JTextArea();
        javax.swing.JScrollPane modsScrollPane = new javax.swing.JScrollPane();
        mods = new javax.swing.JTextArea();
        javax.swing.JScrollPane archivesScrollPane = new javax.swing.JScrollPane();
        archives = new javax.swing.JTextArea();
        serverList = new javax.swing.JLabel();
        mainClassScrollPane = new javax.swing.JScrollPane();
        mainClass = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        packName.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        packName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        packName.setText("Mod Pack Name");

        versionLabel.setText("Version:");

        minecraftLabel.setText("Minecraft:");

        txtLabel.setText("Texperience:");

        mainClassLabel.setText("Main Class:");

        mcArgsLabel.setText("Arguments:");

        libsLabel.setText("Libraries:");

        modsLabel.setText("Mods:");

        zipsLabel.setText("Archives:");

        serverLabel.setText("Server List:");

        denyButton.setText("Deny");
        denyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                denyButtonActionPerformed(evt);
            }
        });

        acceptButton.setText("Accept");
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed(evt);
            }
        });

        version.setText("null");

        mcVersion.setText("null");

        txtVersion.setText("Not Included");

        argumentsScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        argumentsScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        arguments.setEditable(false);
        arguments.setColumns(20);
        arguments.setLineWrap(true);
        arguments.setRows(2);
        arguments.setText("(Default)");
        arguments.setWrapStyleWord(true);
        argumentsScrollPane.setViewportView(arguments);

        librariesScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        librariesScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        libraries.setEditable(false);
        libraries.setColumns(20);
        libraries.setRows(3);
        librariesScrollPane.setViewportView(libraries);

        modsScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        modsScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        mods.setEditable(false);
        mods.setColumns(20);
        mods.setRows(3);
        modsScrollPane.setViewportView(mods);

        archivesScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        archivesScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        archives.setEditable(false);
        archives.setColumns(20);
        archives.setRows(3);
        archivesScrollPane.setViewportView(archives);

        serverList.setText("Not Included");

        mainClassScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        mainClassScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        mainClass.setEditable(false);
        mainClass.setColumns(20);
        mainClass.setRows(1);
        mainClass.setText("(Default)");
        mainClassScrollPane.setViewportView(mainClass);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(packName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(denyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(mainClassLabel)
                                .addGap(16, 16, 16)
                                .addComponent(mainClassScrollPane))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtLabel)
                                            .addComponent(minecraftLabel)
                                            .addComponent(versionLabel))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(version)
                                            .addComponent(mcVersion)
                                            .addComponent(txtVersion)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(0, 0, 0)
                                                .addComponent(serverList))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(mcArgsLabel)
                                            .addComponent(libsLabel))
                                        .addGap(15, 15, 15)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(argumentsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(librariesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(modsLabel)
                                            .addComponent(zipsLabel))
                                        .addGap(33, 33, 33)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(0, 0, 0)
                                                .addComponent(archivesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(modsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(serverLabel)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(packName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(versionLabel)
                    .addComponent(version))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(minecraftLabel)
                    .addComponent(mcVersion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainClassLabel)
                    .addComponent(mainClassScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mcArgsLabel)
                    .addComponent(argumentsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(libsLabel)
                    .addComponent(librariesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(modsLabel)
                    .addComponent(modsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(zipsLabel)
                    .addComponent(archivesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLabel)
                    .addComponent(txtVersion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(serverLabel)
                    .addComponent(serverList))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(denyButton)
                    .addComponent(acceptButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        try {
            GawdScapeLauncher.modpacks.addCustomPack(id, url);
            JOptionPane.showMessageDialog(this, "Added Mod Pack: " + id);
            GawdScapeLauncher.launcherFrame.loadPacks();
        } catch (IOException ex) {
            GawdScapeLauncher.logger.log(Level.SEVERE, "Error adding new mod pack.", ex);
        }
        dispose();
    }//GEN-LAST:event_acceptButtonActionPerformed

    private void denyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_denyButtonActionPerformed
        dispose();
    }//GEN-LAST:event_denyButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea archives;
    private javax.swing.JTextArea arguments;
    private javax.swing.JTextArea libraries;
    private javax.swing.JTextArea mainClass;
    private javax.swing.JLabel mainClassLabel;
    private javax.swing.JScrollPane mainClassScrollPane;
    private javax.swing.JLabel mcVersion;
    private javax.swing.JTextArea mods;
    private javax.swing.JLabel packName;
    private javax.swing.JLabel serverList;
    private javax.swing.JLabel txtVersion;
    private javax.swing.JLabel version;
    // End of variables declaration//GEN-END:variables
}
