package com.afs.gui;

import com.afs.gui.panels.ModuleManagementPanel;
import com.afs.gui.panels.ProfilePanel;
import com.afs.gui.panels.ReportsPanel;
import com.afs.gui.panels.TimetablePanel;
import com.afs.models.User;
import com.afs.utils.Theme;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AcademicLeaderDashboard extends DashboardPanel {

    public AcademicLeaderDashboard(MainFrame mainFrame, User currentUser) {
        super(mainFrame, currentUser);
        showWelcome();
    }

    @Override
    protected void addMenuItems(JPanel sidebar) {
        addSidebarButton("Manage Modules", "MODULES", new ModuleManagementPanel(currentUser));
        addSidebarButton("View Reports", "REPORTS", new ReportsPanel(currentUser));
        addSidebarButton("Timetable", "TIMETABLE", new TimetablePanel(currentUser));
        addSidebarButton("Edit Profile", "PROFILE", new ProfilePanel(currentUser));
    }
    
    private void showWelcome() {
        JPanel welcome = new JPanel(new BorderLayout());
        welcome.setBackground(Theme.BACKGROUND_COLOR);
        JLabel lbl = new JLabel("Welcome Academic Leader.", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        welcome.add(lbl, BorderLayout.CENTER);
        contentPanel.add(welcome, "WELCOME");
        ((java.awt.CardLayout)contentPanel.getLayout()).show(contentPanel, "WELCOME");
    }

    private JPanel createPlaceholderPanel(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BACKGROUND_COLOR);
        p.add(new JLabel("Placeholder: " + title, SwingConstants.CENTER), BorderLayout.CENTER);
        return p;
    }
}
