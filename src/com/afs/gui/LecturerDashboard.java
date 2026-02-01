package com.afs.gui;

import com.afs.gui.panels.AssessmentDesignPanel;
import com.afs.gui.panels.CommentsPanel;
import com.afs.gui.panels.GradingPanel;
import com.afs.gui.panels.ProfilePanel;
import com.afs.gui.panels.TimetablePanel;
import com.afs.models.User;
import com.afs.utils.Theme;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LecturerDashboard extends DashboardPanel {

    public LecturerDashboard(MainFrame mainFrame, User currentUser) {
        super(mainFrame, currentUser);
        showWelcome();
    }

    @Override
    protected void addMenuItems(JPanel sidebar) {
        addSidebarButton("Assessments", "ASSESSMENTS", new AssessmentDesignPanel(currentUser));
        addSidebarButton("Grading", "GRADING", new GradingPanel(currentUser));
        addSidebarButton("Timetable", "TIMETABLE", new TimetablePanel(currentUser));
        addSidebarButton("Comments", "COMMENTS", new CommentsPanel(currentUser));
        addSidebarButton("Edit Profile", "PROFILE", new ProfilePanel(currentUser));
    }
    
    private void showWelcome() {
        JPanel welcome = new JPanel(new BorderLayout());
        welcome.setBackground(Theme.BACKGROUND_COLOR);
        JLabel lbl = new JLabel("Welcome Lecturer.", SwingConstants.CENTER);
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
