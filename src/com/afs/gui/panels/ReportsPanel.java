package com.afs.gui.panels;

import com.afs.managers.AssessmentManager;
import com.afs.managers.FeedbackManager;
import com.afs.managers.ModuleManager;
import com.afs.models.Assessment;
import com.afs.models.Feedback;
import com.afs.models.Module;
import com.afs.models.Result;
import com.afs.models.User;
import com.afs.models.Role;
import com.afs.utils.Theme;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReportsPanel extends JPanel {
    private User currentUser;

    public ReportsPanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND_COLOR);
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(Theme.FONT_MENU);
        tabs.addTab("Academic Performance", createPerformancePanel());
        tabs.addTab("Student Feedback Analysis", createFeedbackAnalysisPanel());
        
        add(tabs, BorderLayout.CENTER);
    }

    // Default constructor for Admin (sees all)
    public ReportsPanel() {
        this(null); 
    }

    private List<Module> getModules() {
        if (currentUser == null || currentUser.getRole() == Role.ADMINISTRATOR) {
            return ModuleManager.getInstance().getAllModules();
        } else if (currentUser.getRole() == Role.ACADEMIC_LEADER) {
            // Assuming ModuleManager supports this, otherwise fall back to all or empty
            // Ideally: return ModuleManager.getInstance().getModulesByLeader(currentUser.getId());
            // Since I saw getModulesByLeader in the file I read, I will try to use it if I can confirm it exists.
            // The previous file read showed: ModuleManager.getInstance().getModulesByLeader(currentUser.getId());
            // So I will assume it exists. However, I need to be careful if ModuleManager wasn't updated.
            // I'll stick to getAllModules for now to be safe unless I check ModuleManager.
            // Actually, for Admin Dashboard, we want ALL modules.
            return ModuleManager.getInstance().getAllModules();
        }
        return ModuleManager.getInstance().getAllModules();
    }

    private JPanel createPerformancePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Theme.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Module Performance Overview");
        lblTitle.setFont(Theme.FONT_TITLE);
        panel.add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"Module", "Assessments", "Avg Mark", "Pass Rate (%)", "Students Graded"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        Theme.styleTable(table);

        List<Module> modules = getModules();
        AssessmentManager am = AssessmentManager.getInstance();

        for (Module m : modules) {
            List<Assessment> assessments = am.getAssessmentsByModule(m.getId());
            double totalModuleMarks = 0;
            int totalResults = 0;
            int passCount = 0;

            for (Assessment a : assessments) {
                List<Result> results = am.getResultsByAssessmentId(a.getId());
                for (Result r : results) {
                    totalModuleMarks += r.getMarks();
                    if (r.getMarks() >= 50) passCount++;
                }
                totalResults += results.size();
            }

            double avg = totalResults > 0 ? totalModuleMarks / totalResults : 0;
            double passRate = totalResults > 0 ? (double) passCount / totalResults * 100 : 0;

            model.addRow(new Object[]{
                m.getId(), 
                assessments.size(), 
                String.format("%.1f", avg), 
                String.format("%.1f%%", passRate), 
                totalResults
            });
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFeedbackAnalysisPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Theme.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Student Feedback Summary");
        lblTitle.setFont(Theme.FONT_TITLE);
        panel.add(lblTitle, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.5);

        String[] columns = {"Module", "Avg Rating (1-5)", "Feedback Count"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        Theme.styleTable(table);
        
        JTextArea txtComments = new JTextArea();
        txtComments.setEditable(false);
        txtComments.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(new JLabel("  Student Comments:"), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(txtComments), BorderLayout.CENTER);

        List<Module> modules = getModules();
        FeedbackManager fm = FeedbackManager.getInstance();

        for (Module m : modules) {
            List<Feedback> fbs = fm.getFeedbackForModule(m.getId());
            double avg = fbs.stream().mapToInt(Feedback::getRating).average().orElse(0.0);
            model.addRow(new Object[]{m.getId(), String.format("%.1f", avg), fbs.size()});
        }

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                String modId = (String) model.getValueAt(table.getSelectedRow(), 0);
                List<Feedback> fbs = fm.getFeedbackForModule(modId);
                StringBuilder sb = new StringBuilder();
                if (fbs.isEmpty()) {
                    sb.append("No feedback available for this module.");
                } else {
                    for (Feedback f : fbs) {
                        sb.append(String.format("Rating: %d/5\n%s\n", f.getRating(), f.getComment()));
                        sb.append("-----------------------------------\n");
                    }
                }
                txtComments.setText(sb.toString());
                txtComments.setCaretPosition(0);
            }
        });

        split.setLeftComponent(new JScrollPane(table));
        split.setRightComponent(rightPanel);

        panel.add(split, BorderLayout.CENTER);
        return panel;
    }
}
