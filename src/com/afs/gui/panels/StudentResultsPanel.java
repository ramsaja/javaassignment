package com.afs.gui.panels;

import com.afs.managers.AssessmentManager;
import com.afs.managers.GradingManager;
import com.afs.managers.ModuleManager;
import com.afs.models.Assessment;
import com.afs.models.Module;
import com.afs.models.Result;
import com.afs.models.User;
import com.afs.utils.Theme;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentResultsPanel extends JPanel {
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private User currentUser;

    public StudentResultsPanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("My Results");
        title.setFont(Theme.FONT_TITLE);
        add(title, BorderLayout.NORTH);

        String[] columns = {"Module", "Assessment", "Marks", "Grade", "Feedback"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultsTable = new JTable(tableModel);
        resultsTable.setRowHeight(25);
        add(new JScrollPane(resultsTable), BorderLayout.CENTER);
        
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Result> results = AssessmentManager.getInstance().getResultsByStudent(currentUser.getId());
        
        for (Result r : results) {
            String assessmentTitle = "Unknown";
            String moduleName = "Unknown";
            
            Assessment a = AssessmentManager.getInstance().getAssessmentById(r.getAssessmentId());
            if (a != null) {
                assessmentTitle = a.getTitle();
                Module m = ModuleManager.getInstance().getModuleById(a.getModuleId());
                if (m != null) {
                    moduleName = m.getName();
                }
            }
            
            String grade = GradingManager.getInstance().calculateGrade(r.getMarks());
            tableModel.addRow(new Object[]{moduleName, assessmentTitle, r.getMarks(), grade, r.getFeedback()});
        }
    }
}
