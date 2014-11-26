package com.gawdscape.launcher;

import com.gawdscape.launcher.auth.AuthManager;
import com.gawdscape.launcher.auth.SessionManager;
import com.gawdscape.launcher.auth.SessionResponse;
import com.gawdscape.launcher.auth.SessionToken;
import com.gawdscape.launcher.util.Constants;
import com.gawdscape.launcher.util.ImageUtils;
import com.gawdscape.launcher.util.Log;
import com.gawdscape.launcher.util.OperatingSystem;
import java.awt.Color;
import java.util.Iterator;

/**
 *
 * @author Vinnie
 */
public final class LoginDialog extends javax.swing.JDialog {

    private SessionManager sessionManager;

    /**
     * Creates new form LoginDialog
     */
    public LoginDialog(java.awt.Frame parent, boolean modal, SessionManager manager) {
	super(parent, modal);
	sessionManager = manager;
	Log.debug("Initializing login dialog.");
	initComponents();
	setLocationRelativeTo(parent);
	loadUsers();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infoLabel = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        rememberCheckBox = new javax.swing.JCheckBox();
        loginButton = new javax.swing.JButton();
        helpLabel = new javax.swing.JLabel();
        buyLabel = new javax.swing.JLabel();
        usernameComboBox = new javax.swing.JComboBox();
        autoLoginCheckBox = new javax.swing.JCheckBox();

        setTitle("GawdScape Launcher");
        setIconImage(ImageUtils.getFavIcon());
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        infoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoLabel.setText("<html>Please login using your Minecraft acccount.");

        usernameLabel.setText("Username or Email:");

        passwordLabel.setText("Password:");

        rememberCheckBox.setText("Remember account?");

        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        helpLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        helpLabel.setText("Launcher Help");
        helpLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        helpLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                helpLabelMouseClicked(evt);
            }
        });

        buyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        buyLabel.setText("Buy Minecraft");
        buyLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        buyLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buyLabelMouseClicked(evt);
            }
        });

        usernameComboBox.setEditable(true);
        usernameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameComboBoxActionPerformed(evt);
            }
        });

        autoLoginCheckBox.setText("Auto login?");
        autoLoginCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoLoginCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(infoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(autoLoginCheckBox)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(usernameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(passwordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(passwordField)
                        .addComponent(rememberCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                        .addComponent(loginButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(helpLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(usernameComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(infoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(passwordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(rememberCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(autoLoginCheckBox)
                .addGap(18, 18, 18)
                .addComponent(loginButton)
                .addGap(18, 18, 18)
                .addComponent(helpLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buyLabel)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
	String username = usernameComboBox.getSelectedItem().toString();
	String password = new String(passwordField.getPassword());

	if (sessionManager.isUserSaved(username)) {
	    doLogin(null, null, sessionManager.getToken(username));
	    return;
	}

	if ((username.isEmpty()) && (password.isEmpty())) {
	    setError("Please enter a username and password.");
	} else if (username.isEmpty()) {
	    setError("Please enter a username.");
	} else if (password.isEmpty()) {
	    setError("Please enter a password.");
	} else {
	    doLogin(username, password, null);
	}
    }//GEN-LAST:event_loginButtonActionPerformed

    private void helpLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helpLabelMouseClicked
	OperatingSystem.openLink(Constants.LAUNCHER_WIKI_LINK);
    }//GEN-LAST:event_helpLabelMouseClicked

    private void buyLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buyLabelMouseClicked
	OperatingSystem.openLink(Constants.MC_BUY_LINK);
    }//GEN-LAST:event_buyLabelMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
	System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    private void autoLoginCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoLoginCheckBoxActionPerformed
        if (autoLoginCheckBox.isSelected())
	    rememberCheckBox.setSelected(true);
    }//GEN-LAST:event_autoLoginCheckBoxActionPerformed

    private void usernameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameComboBoxActionPerformed
        String username = usernameComboBox.getSelectedItem().toString();
	boolean saved = sessionManager.isUserSaved(username);
	boolean autoLogin = sessionManager.isAutoLoginUser(username);
	usernameComboBox.setEditable(!saved);
	passwordField.setEditable(!saved);
	rememberCheckBox.setSelected(saved);
	autoLoginCheckBox.setSelected(autoLogin);
    }//GEN-LAST:event_usernameComboBoxActionPerformed

    public void loadUsers() {
	Iterator it = sessionManager.getSavedUsernames().iterator();
	while (it.hasNext()) {
	    usernameComboBox.addItem(it.next());
	}
	if (sessionManager.getLastUser() != null) {
	    usernameComboBox.setSelectedItem(sessionManager.getLastUser());
	}
	usernameComboBox.addItem("");
    }

    public void setError(String text) {
	infoLabel.setForeground(Color.red);
	infoLabel.setText("<html>" + text);
	Log.severe("Error logging in:");
	Log.severe(text);
    }

    public void doLogin(String username, String password, SessionToken token) {
	    SessionResponse response;
	    if (token != null) {
		response = AuthManager.refresh(token);
	    } else {
		response = AuthManager.authenticate(username, password, null);
	    }
	    GawdScapeLauncher.session = response;

	    if (response.getAccessToken() != null) {
		String playerName = response.getSelectedProfile().getName();
		if (rememberCheckBox.isSelected()) {
		    sessionManager.addSession(response);
		    sessionManager.setLastUser(playerName);
		    if (autoLoginCheckBox.isSelected()) {
			sessionManager.setAutoLoginUser(playerName);
		    }
		} else {
		    sessionManager.removeSession(playerName);
		    sessionManager.setLastUser(null);
		}
		if (!autoLoginCheckBox.isSelected() && sessionManager.isAutoLoginUser(playerName)) {
		    sessionManager.setAutoLoginUser(null);
		}
		SessionManager.saveSessions(sessionManager);
		dispose();
		GawdScapeLauncher.launcherFrame.setUsername(playerName);
		GawdScapeLauncher.launcherFrame.setVisible(true);
                GawdScapeLauncher.loginDialog = null;
	    } else {
		setError(response.getErrorMessage());
	    }
    }

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
		LoginDialog dialog = new LoginDialog(new javax.swing.JFrame(), true, SessionManager.loadSessions());
		dialog.setVisible(true);
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox autoLoginCheckBox;
    private javax.swing.JLabel buyLabel;
    private javax.swing.JLabel helpLabel;
    public static javax.swing.JLabel infoLabel;
    private javax.swing.JButton loginButton;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JCheckBox rememberCheckBox;
    private javax.swing.JComboBox usernameComboBox;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
