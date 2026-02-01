package com.afs.gui;

import com.afs.models.User;
import com.afs.utils.Theme;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    
    // Dashboards
    private JPanel adminDashboard; // Placeholder
    private JPanel academicLeaderDashboard; // Placeholder
    private JPanel lecturerDashboard; // Placeholder
    private JPanel studentDashboard; // Placeholder

    public MainFrame() {
        setTitle("Assessment Feedback System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        loginPanel = new LoginPanel(this);
        mainPanel.add(loginPanel, "LOGIN");

        add(mainPanel);
        
        // Ensure data directories exist
        com.afs.data.FileManager.ensureDataDirectory();
        
        setVisible(true);
    }

    public void showDashboard(User user) {
        // Initialize dashboards lazily or recreate them to refresh data
        switch (user.getRole()) {
            case ADMINISTRATOR:
                if (adminDashboard == null) adminDashboard = new AdminDashboard(this, user);
                else ((AdminDashboard)adminDashboard).refresh(); // Assuming refresh method exists
                if (!isComponentAdded(adminDashboard)) mainPanel.add(adminDashboard, "ADMIN");
                cardLayout.show(mainPanel, "ADMIN");
                break;
            case ACADEMIC_LEADER:
                if (academicLeaderDashboard == null) academicLeaderDashboard = new AcademicLeaderDashboard(this, user);
                 else ((AcademicLeaderDashboard)academicLeaderDashboard).refresh();
                if (!isComponentAdded(academicLeaderDashboard)) mainPanel.add(academicLeaderDashboard, "LEADER");
                cardLayout.show(mainPanel, "LEADER");
                break;
            case LECTURER:
                if (lecturerDashboard == null) lecturerDashboard = new LecturerDashboard(this, user);
                 else ((LecturerDashboard)lecturerDashboard).refresh();
                if (!isComponentAdded(lecturerDashboard)) mainPanel.add(lecturerDashboard, "LECTURER");
                cardLayout.show(mainPanel, "LECTURER");
                break;
            case STUDENT:
                if (studentDashboard == null) studentDashboard = new StudentDashboard(this, user);
                 else ((StudentDashboard)studentDashboard).refresh();
                if (!isComponentAdded(studentDashboard)) mainPanel.add(studentDashboard, "STUDENT");
                cardLayout.show(mainPanel, "STUDENT");
                break;
        }
    }
    
    private boolean isComponentAdded(JPanel panel) {
        for (java.awt.Component comp : mainPanel.getComponents()) {
            if (comp == panel) return true;
        }
        return false;
    }

    public void logout() {
        cardLayout.show(mainPanel, "LOGIN");
        // Clear fields
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
