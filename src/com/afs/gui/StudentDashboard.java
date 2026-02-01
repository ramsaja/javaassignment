package com.afs.gui;

import com.afs.gui.panels.CommentsPanel;
import com.afs.gui.panels.EnrollmentPanel;
import com.afs.gui.panels.ProfilePanel;
import com.afs.gui.panels.StudentFeedbackPanel;
import com.afs.gui.panels.StudentResultsPanel;
import com.afs.gui.panels.TimetablePanel;
import com.afs.models.User;
import com.afs.utils.Theme;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class StudentDashboard extends DashboardPanel {

    public StudentDashboard(MainFrame mainFrame, User currentUser) {
        super(mainFrame, currentUser);
        showWelcome();
    }

    @Override
    protected void addMenuItems(JPanel sidebar) {
        addSidebarButton("Enrollment", "ENROLLMENT", new EnrollmentPanel(currentUser));
        addSidebarButton("My Results", "RESULTS", new StudentResultsPanel(currentUser));
        addSidebarButton("Timetable", "TIMETABLE", new TimetablePanel(currentUser));
        addSidebarButton("Edit Profile", "PROFILE", new ProfilePanel(currentUser));
        addSidebarButton("Comments", "COMMENTS", new CommentsPanel(currentUser));
    }
    
    private void showWelcome() {
        JPanel welcome = new JPanel(new BorderLayout());
        welcome.setBackground(Theme.BACKGROUND_COLOR);
        JLabel lbl = new JLabel("Welcome Student.", SwingConstants.CENTER);
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
