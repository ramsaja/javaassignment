package com.afs.gui;

import com.afs.gui.panels.AssignLecturerPanel;
import com.afs.gui.panels.ClassManagementPanel;
import com.afs.gui.panels.GradingSystemPanel;
import com.afs.gui.panels.ReportsPanel;
import com.afs.gui.panels.UserManagementPanel;
import com.afs.managers.ClassManager;
import com.afs.managers.ModuleManager;
import com.afs.managers.UserManager;
import com.afs.models.User;
import com.afs.utils.Theme;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AdminDashboard extends DashboardPanel {

    private JLabel lblUserCount;
    private JLabel lblModuleCount;
    private JLabel lblClassCount;

    public AdminDashboard(MainFrame mainFrame, User currentUser) {
        super(mainFrame, currentUser);
        ((java.awt.CardLayout)contentPanel.getLayout()).show(contentPanel, "WELCOME");
    }

    @Override
    protected void addMenuItems(JPanel sidebar) {
        addSidebarButton("📊 Dashboard", "WELCOME", createWelcomePanel());
        addSidebarButton("👤 Manage Users", "USERS", new UserManagementPanel());
        addSidebarButton("👨‍🏫 Assign Lecturers", "ASSIGN", new AssignLecturerPanel());
        addSidebarButton("📚 Grading System", "GRADING", new GradingSystemPanel());
        addSidebarButton("🗓️ Module Classes", "CLASSES", new ClassManagementPanel());
        addSidebarButton("📈 Reports & Analysis", "REPORTS", new ReportsPanel());
    }
    
    private JPanel createWelcomePanel() {
        JPanel welcome = new JPanel(new BorderLayout(20, 20));
        welcome.setBackground(Theme.BACKGROUND_COLOR);
        welcome.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Header
        JLabel lbl = new JLabel("Dashboard Overview");
        lbl.setFont(Theme.FONT_TITLE);
        welcome.add(lbl, BorderLayout.NORTH);
        
        // Cards Panel
        JPanel cardsPanel = new JPanel(new java.awt.GridLayout(1, 3, 20, 0));
        cardsPanel.setBackground(Theme.BACKGROUND_COLOR);
        
        lblUserCount = createCountLabel();
        lblModuleCount = createCountLabel();
        lblClassCount = createCountLabel();
        
        cardsPanel.add(createSummaryCard("Total Users", lblUserCount, Theme.CARD_BLUE));
        cardsPanel.add(createSummaryCard("Active Modules", lblModuleCount, Theme.CARD_GREEN));
        cardsPanel.add(createSummaryCard("Scheduled Classes", lblClassCount, Theme.CARD_ORANGE));
        
        // Container to keep cards at top
        JPanel cardsContainer = new JPanel(new BorderLayout());
        cardsContainer.setBackground(Theme.BACKGROUND_COLOR);
        cardsContainer.add(cardsPanel, BorderLayout.NORTH);
        
        welcome.add(cardsContainer, BorderLayout.CENTER);
        
        updateCounts();
        
        return welcome;
    }
    
    private JLabel createCountLabel() {
        JLabel lbl = new JLabel("0");
        lbl.setForeground(java.awt.Color.WHITE);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 36));
        return lbl;
    }
    
    private JPanel createSummaryCard(String title, JLabel countLabel, java.awt.Color bg) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(java.awt.Color.WHITE);
        lblTitle.setFont(Theme.FONT_REGULAR);
        
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(countLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void updateCounts() {
        if (lblUserCount != null)
            lblUserCount.setText(String.valueOf(UserManager.getInstance().getAllUsers().size()));
        if (lblModuleCount != null)
            lblModuleCount.setText(String.valueOf(ModuleManager.getInstance().getAllModules().size()));
        if (lblClassCount != null)
            lblClassCount.setText(String.valueOf(ClassManager.getInstance().getAllClasses().size()));
    }
    
    @Override
    protected void refreshCurrentPanel(String cardName) {
        if ("WELCOME".equals(cardName)) {
            updateCounts();
        }
    }

    private JPanel createPlaceholderPanel(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BACKGROUND_COLOR);
        p.add(new JLabel("Placeholder: " + title, SwingConstants.CENTER), BorderLayout.CENTER);
        return p;
    }
}
