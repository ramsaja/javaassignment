package com.afs.gui.panels;

import com.afs.gui.components.ModernButton;
import com.afs.gui.components.ModernTextField;
import com.afs.managers.AssessmentManager;
import com.afs.managers.ModuleManager;
import com.afs.managers.UserManager;
import com.afs.models.Assessment;
import com.afs.models.Result;
import com.afs.models.Role;
import com.afs.models.User;
import com.afs.models.Module;
import com.afs.utils.Theme;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GradingPanel extends JPanel {
    private JComboBox<String> cmbModules;
    private JComboBox<String> cmbAssessments;
    private JTable gradingTable;
    private DefaultTableModel tableModel;
    private User currentUser;
    private ModernTextField txtMarks;
    private JTextArea txtFeedback;

    public GradingPanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top: Filters & Actions
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Theme.BACKGROUND_COLOR);
        
        JPanel filterPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        filterPanel.setBackground(Theme.BACKGROUND_COLOR);
        
        filterPanel.add(new JLabel("Module:"));
        cmbModules = new JComboBox<>();
        cmbModules.addActionListener(e -> loadAssessments());
        filterPanel.add(cmbModules);
        
        filterPanel.add(new JLabel("Assessment:"));
        cmbAssessments = new JComboBox<>();
        cmbAssessments.addActionListener(e -> loadStudents());
        filterPanel.add(cmbAssessments);
        
        top.add(filterPanel, BorderLayout.CENTER);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(Theme.BACKGROUND_COLOR);
        ModernButton btnAddStudent = new ModernButton("Add Student", Theme.ACCENT_COLOR, Theme.ACCENT_COLOR.darker());
        btnAddStudent.addActionListener(e -> addStudentToModule());
        actionPanel.add(btnAddStudent);
        
        top.add(actionPanel, BorderLayout.EAST);
        
        add(top, BorderLayout.NORTH);

        // Center: Table of Students
        String[] columns = {"Student ID", "Name", "Marks", "Feedback"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        gradingTable = new JTable(tableModel);
        gradingTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && gradingTable.getSelectedRow() != -1) {
                loadSelection(gradingTable.getSelectedRow());
            }
        });
        add(new JScrollPane(gradingTable), BorderLayout.CENTER);
        
        // Bottom: Editing Form
        JPanel editPanel = new JPanel(new BorderLayout());
        editPanel.setBackground(Theme.BACKGROUND_COLOR);
        editPanel.setBorder(BorderFactory.createTitledBorder("Enter Marks & Feedback"));
        
        JPanel inputs = new JPanel(new GridLayout(2, 2, 5, 5));
        inputs.setBackground(Theme.BACKGROUND_COLOR);
        txtMarks = new ModernTextField();
        txtFeedback = new JTextArea(3, 20);
        txtFeedback.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        inputs.add(new JLabel("Marks:"));
        inputs.add(txtMarks);
        inputs.add(new JLabel("Feedback:"));
        inputs.add(new JScrollPane(txtFeedback));
        
        editPanel.add(inputs, BorderLayout.CENTER);
        
        ModernButton btnSave = new ModernButton("Save Result", Theme.PRIMARY_COLOR, Theme.PRIMARY_COLOR.darker());
        btnSave.addActionListener(e -> saveResult());
        editPanel.add(btnSave, BorderLayout.SOUTH);
        
        add(editPanel, BorderLayout.SOUTH);

        loadModules();
    }

    private void loadModules() {
        cmbModules.removeAllItems();
        List<Module> modules = ModuleManager.getInstance().getModulesByLecturer(currentUser.getId());
        for (Module m : modules) {
            cmbModules.addItem(m.getId());
        }
    }

    private void loadAssessments() {
        cmbAssessments.removeAllItems();
        String moduleId = (String) cmbModules.getSelectedItem();
        if (moduleId != null) {
            List<Assessment> assessments = AssessmentManager.getInstance().getAssessmentsByModule(moduleId);
            for (Assessment a : assessments) {
                cmbAssessments.addItem(a.getId());
            }
        }
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        String moduleId = (String) cmbModules.getSelectedItem();
        String assessmentId = (String) cmbAssessments.getSelectedItem();
        
        if (moduleId != null && assessmentId != null) {
            // Get students enrolled in this module
            List<User> students = UserManager.getInstance().getAllUsers().stream()
                .filter(u -> u.getRole() == Role.STUDENT)
                .filter(u -> ModuleManager.getInstance().isStudentEnrolled(u.getId(), moduleId))
                .collect(Collectors.toList());
            
            for (User s : students) {
                Result r = AssessmentManager.getInstance().getResult(s.getId(), assessmentId);
                String marks = (r != null) ? String.valueOf(r.getMarks()) : "0.0";
                String feedback = (r != null && r.getFeedback() != null) ? r.getFeedback() : "";
                tableModel.addRow(new Object[]{s.getId(), s.getFullName(), marks, feedback});
            }
        }
    }
    
    private void loadSelection(int row) {
        Object marksObj = tableModel.getValueAt(row, 2);
        Object feedbackObj = tableModel.getValueAt(row, 3);
        
        txtMarks.setText(marksObj != null ? marksObj.toString() : "");
        txtFeedback.setText(feedbackObj != null ? feedbackObj.toString() : "");
    }

    private void saveResult() {
        int row = gradingTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a student first.");
            return;
        }
        
        String studentId = (String) tableModel.getValueAt(row, 0);
        String assessmentId = (String) cmbAssessments.getSelectedItem();
        
        try {
            double marks = Double.parseDouble(txtMarks.getText());
            String feedback = txtFeedback.getText();
            
            Result r = new Result(studentId, assessmentId, marks, feedback);
            AssessmentManager.getInstance().addOrUpdateResult(r);
            loadStudents(); // refresh
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid marks.");
        }
    }
    
    private void addStudentToModule() {
        String moduleId = (String) cmbModules.getSelectedItem();
        if (moduleId == null) {
            JOptionPane.showMessageDialog(this, "Select a module first.");
            return;
        }

        String studentId = JOptionPane.showInputDialog(this, "Enter Student ID to enroll:");
        if (studentId != null && !studentId.trim().isEmpty()) {
            studentId = studentId.trim();
            User user = UserManager.getInstance().getUserById(studentId);
            
            if (user == null || user.getRole() != Role.STUDENT) {
                 JOptionPane.showMessageDialog(this, "Invalid Student ID.");
                 return;
            }
            
            if (ModuleManager.getInstance().isStudentEnrolled(studentId, moduleId)) {
                 JOptionPane.showMessageDialog(this, "Student is already enrolled.");
                 return;
            }
            
            ModuleManager.getInstance().enrollStudent(studentId, moduleId);
            JOptionPane.showMessageDialog(this, "Student enrolled successfully.");
            loadStudents();
        }
    }
}
