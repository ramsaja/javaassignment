package com.afs.gui.panels;

import com.afs.gui.components.ModernButton;
import com.afs.managers.FeedbackManager;
import com.afs.managers.ModuleManager;
import com.afs.models.Feedback;
import com.afs.models.Module;
import com.afs.models.User;
import com.afs.utils.Theme;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class StudentFeedbackPanel extends JPanel {
    private User currentUser;
    private JComboBox<String> cmbModules;
    private JComboBox<Integer> cmbRating;
    private JTextArea txtComment;

    public StudentFeedbackPanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Card Panel
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        JLabel lblTitle = new JLabel("Module Feedback");
        lblTitle.setFont(Theme.FONT_TITLE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        cardPanel.add(lblTitle, BorderLayout.NORTH);

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Module Selection
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Select Module:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        cmbModules = new JComboBox<>();
        loadModules();
        formPanel.add(cmbModules, gbc);

        // Rating
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Rating (1-5):"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        cmbRating = new JComboBox<>(new Integer[]{5, 4, 3, 2, 1});
        formPanel.add(cmbRating, gbc);

        // Comment
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Comments:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        txtComment = new JTextArea(5, 20);
        txtComment.setLineWrap(true);
        txtComment.setWrapStyleWord(true);
        txtComment.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        formPanel.add(new JScrollPane(txtComment), gbc);

        cardPanel.add(formPanel, BorderLayout.CENTER);

        // Submit Button
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        ModernButton btnSubmit = new ModernButton("Submit Feedback", Theme.PRIMARY_COLOR, Theme.PRIMARY_COLOR.darker());
        btnSubmit.addActionListener(e -> submitFeedback());
        btnPanel.add(btnSubmit);
        
        cardPanel.add(btnPanel, BorderLayout.SOUTH);

        add(cardPanel, BorderLayout.CENTER);
    }

    private void loadModules() {
        cmbModules.removeAllItems();
        List<Module> modules = ModuleManager.getInstance().getAllModules();
        // In a real app, we would filter by enrollment here
        for (Module m : modules) {
            cmbModules.addItem(m.getId());
        }
    }

    private void submitFeedback() {
        String moduleId = (String) cmbModules.getSelectedItem();
        if (moduleId == null) {
            JOptionPane.showMessageDialog(this, "Please select a module.");
            return;
        }

        String comment = txtComment.getText().trim();
        if (comment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a comment.");
            return;
        }

        int rating = (Integer) cmbRating.getSelectedItem();

        Feedback fb = new Feedback(currentUser.getId(), moduleId, rating, comment);
        FeedbackManager.getInstance().addFeedback(fb);

        JOptionPane.showMessageDialog(this, "Feedback submitted successfully!");
        txtComment.setText("");
        cmbRating.setSelectedIndex(0);
    }
}
