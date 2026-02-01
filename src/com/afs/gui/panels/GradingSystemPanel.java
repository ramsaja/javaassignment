package com.afs.gui.panels;

import com.afs.gui.components.ModernButton;
import com.afs.gui.components.ModernTextField;
import com.afs.managers.GradingManager;
import com.afs.models.GradeRule;
import com.afs.utils.Theme;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GradingSystemPanel extends JPanel {
    private JTable ruleTable;
    private DefaultTableModel tableModel;
    private ModernTextField txtGrade, txtMin, txtMax;

    public GradingSystemPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblHeader = new JLabel("Grade Rule");
        lblHeader.setFont(Theme.FONT_BOLD);
        cardPanel.add(lblHeader, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);

        txtGrade = new ModernTextField();
        txtMin = new ModernTextField();
        txtMax = new ModernTextField();

        formPanel.add(new JLabel("Grade (e.g., A):"));
        formPanel.add(txtGrade);
        formPanel.add(new JLabel("Min Mark:"));
        formPanel.add(txtMin);
        formPanel.add(new JLabel("Max Mark:"));
        formPanel.add(txtMax);
        
        cardPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        ModernButton btnAdd = new ModernButton("Add", Theme.PRIMARY_COLOR, Theme.PRIMARY_COLOR.darker());
        ModernButton btnUpdate = new ModernButton("Update", Theme.ACCENT_COLOR, Theme.ACCENT_COLOR.darker());
        ModernButton btnDelete = new ModernButton("Delete", Theme.DANGER_COLOR, Theme.DANGER_COLOR.darker());
        ModernButton btnClear = new ModernButton("Clear", Theme.SIDEBAR_COLOR, Theme.SIDEBAR_COLOR.darker());

        btnAdd.addActionListener(e -> addRule());
        btnUpdate.addActionListener(e -> updateRule());
        btnDelete.addActionListener(e -> deleteRule());
        btnClear.addActionListener(e -> clearForm());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Theme.BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        topPanel.add(cardPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Grade", "Min Mark", "Max Mark"};
        tableModel = new DefaultTableModel(columns, 0);
        ruleTable = new JTable(tableModel);
        Theme.styleTable(ruleTable);
        
        ruleTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && ruleTable.getSelectedRow() != -1) {
                loadRuleFromTable(ruleTable.getSelectedRow());
            }
        });

        add(new JScrollPane(ruleTable), BorderLayout.CENTER);

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<GradeRule> rules = GradingManager.getInstance().getRules();
        for (GradeRule r : rules) {
            tableModel.addRow(new Object[]{r.getGrade(), r.getMinMark(), r.getMaxMark()});
        }
    }

    private void loadRuleFromTable(int row) {
        txtGrade.setText(tableModel.getValueAt(row, 0).toString());
        txtMin.setText(tableModel.getValueAt(row, 1).toString());
        txtMax.setText(tableModel.getValueAt(row, 2).toString());
    }

    private void clearForm() {
        txtGrade.setText("");
        txtMin.setText("");
        txtMax.setText("");
        ruleTable.clearSelection();
    }

    private void addRule() {
        try {
            double min = Double.parseDouble(txtMin.getText());
            double max = Double.parseDouble(txtMax.getText());
            GradeRule rule = new GradeRule(txtGrade.getText(), min, max);
            GradingManager.getInstance().addRule(rule);
            refreshTable();
            clearForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format.");
        }
    }

    private void updateRule() {
        int row = ruleTable.getSelectedRow();
        if (row == -1) return;
        
        try {
            double min = Double.parseDouble(txtMin.getText());
            double max = Double.parseDouble(txtMax.getText());
            GradeRule rule = new GradeRule(txtGrade.getText(), min, max);
            GradingManager.getInstance().updateRule(row, rule);
            refreshTable();
            clearForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format.");
        }
    }

    private void deleteRule() {
        int row = ruleTable.getSelectedRow();
        if (row == -1) return;
        GradingManager.getInstance().removeRule(row);
        refreshTable();
        clearForm();
    }
}
