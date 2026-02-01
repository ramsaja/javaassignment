package com.afs.gui.panels;

import com.afs.gui.components.ModernButton;
import com.afs.gui.components.ModernTextField;
import com.afs.managers.AssessmentManager;
import com.afs.managers.ModuleManager;
import com.afs.models.Assessment;
import com.afs.models.Module;
import com.afs.models.User;
import com.afs.utils.Theme;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AssessmentDesignPanel extends JPanel {
    private JComboBox<String> cmbModules;
    private JTable assessmentTable;
    private DefaultTableModel tableModel;
    private ModernTextField txtId, txtTitle, txtType, txtMaxMarks;
    private User currentUser;

    public AssessmentDesignPanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top: Module Selection
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(Theme.BACKGROUND_COLOR);
        top.add(new JLabel("Select Module:"));
        cmbModules = new JComboBox<>();
        cmbModules.addActionListener(e -> refreshTable());
        top.add(cmbModules);
        
        // Form
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(Theme.BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createTitledBorder("Assessment Details"));
        
        txtId = new ModernTextField();
        txtTitle = new ModernTextField();
        txtType = new ModernTextField();
        txtMaxMarks = new ModernTextField();
        
        formPanel.add(new JLabel("ID:"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Title:"));
        formPanel.add(txtTitle);
        formPanel.add(new JLabel("Type (Quiz/Assignment):"));
        formPanel.add(txtType);
        formPanel.add(new JLabel("Max Marks:"));
        formPanel.add(txtMaxMarks);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Theme.BACKGROUND_COLOR);
        ModernButton btnAdd = new ModernButton("Add Assessment", Theme.PRIMARY_COLOR, Theme.PRIMARY_COLOR.darker());
        btnAdd.addActionListener(e -> addAssessment());
        buttonPanel.add(btnAdd);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(top, BorderLayout.NORTH);
        northPanel.add(formPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(northPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Title", "Type", "Max Marks"};
        tableModel = new DefaultTableModel(columns, 0);
        assessmentTable = new JTable(tableModel);
        add(new JScrollPane(assessmentTable), BorderLayout.CENTER);

        loadModules();
    }

    private void loadModules() {
        cmbModules.removeAllItems();
        List<Module> modules = ModuleManager.getInstance().getModulesByLecturer(currentUser.getId());
        for (Module m : modules) {
            cmbModules.addItem(m.getId());
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        String moduleId = (String) cmbModules.getSelectedItem();
        if (moduleId != null) {
            List<Assessment> assessments = AssessmentManager.getInstance().getAssessmentsByModule(moduleId);
            for (Assessment a : assessments) {
                tableModel.addRow(new Object[]{a.getId(), a.getTitle(), a.getType(), a.getMaxMarks()});
            }
        }
    }

    private void addAssessment() {
        String moduleId = (String) cmbModules.getSelectedItem();
        if (moduleId == null) {
            JOptionPane.showMessageDialog(this, "Select a module first.");
            return;
        }
        
        try {
            int max = Integer.parseInt(txtMaxMarks.getText());
            Assessment a = new Assessment(txtId.getText(), txtTitle.getText(), txtType.getText(), moduleId, max);
            AssessmentManager.getInstance().addAssessment(a);
            refreshTable();
            txtId.setText("");
            txtTitle.setText("");
            txtType.setText("");
            txtMaxMarks.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Max Marks must be a number.");
        }
    }
}
