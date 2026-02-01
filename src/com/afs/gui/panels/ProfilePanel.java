package com.afs.gui.panels;

import com.afs.gui.components.ModernButton;
import com.afs.gui.components.ModernTextField;
import com.afs.managers.UserManager;
import com.afs.models.User;
import com.afs.utils.Theme;
import java.awt.*;
import javax.swing.*;

public class ProfilePanel extends JPanel {
    private User currentUser;
    private ModernTextField txtId;
    private ModernTextField txtUsername;
    private ModernTextField txtFullName;
    private JPasswordField txtPassword;

    public ProfilePanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Main content wrapper for centering
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Theme.BACKGROUND_COLOR);
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 20));
        formPanel.setBackground(Theme.BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Theme.ACCENT_COLOR), 
            "Edit Profile", 
            0, 0, 
            new Font("Segoe UI", Font.BOLD, 14), 
            Theme.TEXT_COLOR
        ));

        // Components
        txtId = new ModernTextField();
        txtId.setText(currentUser.getId());
        txtId.setEditable(false);
        
        txtUsername = new ModernTextField();
        txtUsername.setText(currentUser.getUsername());
        txtUsername.setEditable(false);
        
        txtFullName = new ModernTextField();
        txtFullName.setText(currentUser.getFullName());
        
        txtPassword = new JPasswordField();
        txtPassword.setText(currentUser.getPassword()); // In real app, don't show plaintext
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.ACCENT_COLOR), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtPassword.setBackground(Theme.INPUT_BG_COLOR);
        txtPassword.setForeground(Theme.TEXT_COLOR);
        txtPassword.setCaretColor(Theme.TEXT_COLOR);

        // Add to form
        addLabel(formPanel, "User ID:");
        formPanel.add(txtId);
        
        addLabel(formPanel, "Username:");
        formPanel.add(txtUsername);
        
        addLabel(formPanel, "Full Name:");
        formPanel.add(txtFullName);
        
        addLabel(formPanel, "Password:");
        formPanel.add(txtPassword);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Theme.BACKGROUND_COLOR);
        
        ModernButton btnSave = new ModernButton("Save Changes", Theme.PRIMARY_COLOR, Theme.PRIMARY_COLOR.darker());
        btnSave.setPreferredSize(new Dimension(200, 40));
        btnSave.addActionListener(e -> saveProfile());
        
        buttonPanel.add(btnSave);

        // Layout assembly
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 20, 0);
        
        // Wrapper to limit width
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(Theme.BACKGROUND_COLOR);
        formWrapper.add(formPanel, BorderLayout.CENTER);
        formWrapper.add(buttonPanel, BorderLayout.SOUTH);
        formWrapper.setPreferredSize(new Dimension(500, 300));
        
        contentPanel.add(formWrapper);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void addLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(Theme.TEXT_COLOR);
        panel.add(label);
    }

    private void saveProfile() {
        String newFullName = txtFullName.getText().trim();
        String newPassword = new String(txtPassword.getPassword()).trim();

        if (newFullName.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full Name and Password cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update User object
        currentUser.setFullName(newFullName);
        currentUser.setPassword(newPassword);

        // Save to file
        UserManager.getInstance().updateUser(currentUser);

        JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
