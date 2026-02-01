package com.afs.gui;

import com.afs.gui.components.ModernButton;
import com.afs.models.User;
import com.afs.utils.Theme;
import java.awt.*;
import javax.swing.*;

public abstract class DashboardPanel extends JPanel {
    protected MainFrame mainFrame;
    protected User currentUser;
    protected JPanel contentPanel;
    protected JPanel sidebar;
    private java.util.List<ModernButton> sidebarButtons = new java.util.ArrayList<>();

    public DashboardPanel(MainFrame mainFrame, User currentUser) {
        this.mainFrame = mainFrame;
        this.currentUser = currentUser;
        setLayout(new BorderLayout());

        createContentArea();
        createSidebar();
    }

    private void createSidebar() {
        sidebar = new JPanel();
        sidebar.setBackground(Theme.SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(Theme.SIDEBAR_COLOR);
        header.setLayout(new FlowLayout(FlowLayout.LEFT));
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblName = new JLabel("<html>Welcome,<br>" + currentUser.getFullName() + "</html>");
        lblName.setForeground(Theme.TEXT_COLOR_SIDEBAR);
        lblName.setFont(Theme.FONT_BOLD);
        header.add(lblName);
        
        sidebar.add(header);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Menu Items (to be populated by subclasses)
        addMenuItems(sidebar);
        
        // Logout at bottom
        sidebar.add(Box.createVerticalGlue());
        ModernButton btnLogout = new ModernButton("Logout", Theme.SIDEBAR_COLOR, Theme.DANGER_COLOR);
        btnLogout.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(250, 45));
        btnLogout.addActionListener(e -> mainFrame.logout());
        sidebar.add(btnLogout);
        
        add(sidebar, BorderLayout.WEST);
    }

    private void createContentArea() {
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(Theme.BACKGROUND_COLOR);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    protected void addSidebarButton(String text, String cardName, JPanel panel) {
        contentPanel.add(panel, cardName);
        
        ModernButton btn = new ModernButton(text, Theme.SIDEBAR_COLOR, Theme.PRIMARY_COLOR);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(250, 45));
        btn.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, cardName);
            refreshCurrentPanel(cardName);
        });
        sidebar.add(btn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    protected abstract void addMenuItems(JPanel sidebar);
    
    // Optional hook for refreshing data when switching tabs
    protected void refreshCurrentPanel(String cardName) {}
    
    public void refresh() {
        // Reload user data if needed
    }
}
