package com.afs.gui;

import com.afs.managers.UserManager;
import com.afs.models.User;
import com.afs.gui.components.ModernButton;
import com.afs.gui.components.ModernPasswordField;
import com.afs.gui.components.ModernTextField;
import com.afs.utils.Theme;
import java.awt.*;
import javax.swing.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private ModernTextField txtUsername;
    private ModernPasswordField txtPassword;
    private ModernButton btnLogin;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(Theme.BACKGROUND_COLOR);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));

        JLabel lblTitle = new JLabel("AFS Login");
        lblTitle.setFont(Theme.FONT_TITLE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitle = new JLabel("Assessment Feedback System");
        lblSubtitle.setFont(Theme.FONT_REGULAR);
        lblSubtitle.setForeground(Theme.TEXT_COLOR_SECONDARY);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUsername = new ModernTextField(20);
        txtUsername.setMaximumSize(new Dimension(300, 35));
        
        txtPassword = new ModernPasswordField(20);
        txtPassword.setMaximumSize(new Dimension(300, 35));

        btnLogin = new ModernButton("Login", Theme.SIDEBAR_COLOR, Theme.PRIMARY_COLOR);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(300, 40));
        btnLogin.addActionListener(e -> handleLogin());

        JLabel lblUser = new JLabel("Username");
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblUser.setFont(Theme.FONT_BOLD);
        
        JLabel lblPass = new JLabel("Password");
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPass.setFont(Theme.FONT_BOLD);

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblSubtitle);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(lblUser);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(txtUsername);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(lblPass);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(txtPassword);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(btnLogin);

        add(card);
    }

    private void handleLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        User user = UserManager.getInstance().authenticate(username, password);
        if (user != null) {
            mainFrame.showDashboard(user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
