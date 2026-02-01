package com.afs.gui.panels;

import com.afs.gui.components.ModernButton;
import com.afs.managers.ModuleManager;
import com.afs.models.Module;
import com.afs.models.User;
import com.afs.utils.Theme;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EnrollmentPanel extends JPanel {
    private JTable moduleTable;
    private DefaultTableModel tableModel;
    private User currentUser;

    public EnrollmentPanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("Available Modules");
        title.setFont(Theme.FONT_TITLE);
        add(title, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
             @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        moduleTable = new JTable(tableModel);
        moduleTable.setRowHeight(30);
        add(new JScrollPane(moduleTable), BorderLayout.CENTER);
        
        ModernButton btnEnroll = new ModernButton("Enroll Selected", Theme.PRIMARY_COLOR, Theme.PRIMARY_COLOR.darker());
        btnEnroll.addActionListener(e -> enroll());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Theme.BACKGROUND_COLOR);
        btnPanel.add(btnEnroll);
        add(btnPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Module> allModules = ModuleManager.getInstance().getAllModules();
        for (Module m : allModules) {
            boolean enrolled = ModuleManager.getInstance().isStudentEnrolled(currentUser.getId(), m.getId());
            tableModel.addRow(new Object[]{m.getId(), m.getName(), enrolled ? "Enrolled" : "Available"});
        }
    }

    private void enroll() {
        int row = moduleTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a module to enroll.");
            return;
        }
        
        String moduleId = (String) tableModel.getValueAt(row, 0);
        String status = (String) tableModel.getValueAt(row, 2);
        
        if ("Enrolled".equals(status)) {
            JOptionPane.showMessageDialog(this, "Already enrolled.");
            return;
        }
        
        ModuleManager.getInstance().enrollStudent(currentUser.getId(), moduleId);
        JOptionPane.showMessageDialog(this, "Enrolled successfully!");
        refreshTable();
    }
}
