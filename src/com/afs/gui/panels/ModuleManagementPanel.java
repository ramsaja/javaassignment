package com.afs.gui.panels;

import com.afs.gui.components.ModernButton;
import com.afs.gui.components.ModernTextField;
import com.afs.managers.ModuleManager;
import com.afs.managers.UserManager;
import com.afs.models.Module;
import com.afs.models.Role;
import com.afs.models.User;
import com.afs.utils.Theme;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ModuleManagementPanel extends JPanel {
    private JTable moduleTable;
    private DefaultTableModel tableModel;
    private ModernTextField txtId, txtName, txtLecturers;
    private User currentUser; // The Academic Leader

    public ModuleManagementPanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(Theme.BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createTitledBorder("Module Details"));

        txtId = new ModernTextField();
        txtName = new ModernTextField();
        txtLecturers = new ModernTextField();
        txtLecturers.setToolTipText("Comma separated Lecturer IDs");

        formPanel.add(new JLabel("Module ID:"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Module Name:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Lecturer IDs (csv):"));
        formPanel.add(txtLecturers);
        formPanel.add(new JLabel("Leader ID:"));
        JLabel lblLeader = new JLabel(currentUser.getId());
        formPanel.add(lblLeader);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Theme.BACKGROUND_COLOR);

        ModernButton btnAdd = new ModernButton("Add", Theme.PRIMARY_COLOR, Theme.PRIMARY_COLOR.darker());
        ModernButton btnUpdate = new ModernButton("Update", Theme.ACCENT_COLOR, Theme.ACCENT_COLOR.darker());
        ModernButton btnDelete = new ModernButton("Delete", Theme.DANGER_COLOR, Theme.DANGER_COLOR.darker());
        ModernButton btnClear = new ModernButton("Clear", Theme.SIDEBAR_COLOR, Theme.SIDEBAR_COLOR.darker());

        btnAdd.addActionListener(e -> addModule());
        btnUpdate.addActionListener(e -> updateModule());
        btnDelete.addActionListener(e -> deleteModule());
        btnClear.addActionListener(e -> clearForm());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Theme.BACKGROUND_COLOR);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Leader", "Lecturers"};
        tableModel = new DefaultTableModel(columns, 0);
        moduleTable = new JTable(tableModel);
        moduleTable.setRowHeight(25);
        moduleTable.getTableHeader().setFont(Theme.FONT_BOLD);
        
        moduleTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && moduleTable.getSelectedRow() != -1) {
                loadModuleFromTable(moduleTable.getSelectedRow());
            }
        });

        add(new JScrollPane(moduleTable), BorderLayout.CENTER);

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        // Academic Leader can only see/manage their own modules? 
        // Or all? Prompt says "CRUD new Modules". Usually restricted to their department, but we don't have department.
        // Let's assume they manage modules where they are the leader.
        // But if they are creating new ones, they become the leader.
        // Let's show all modules led by this user.
        List<Module> modules = ModuleManager.getInstance().getModulesByLeader(currentUser.getId());
        for (Module m : modules) {
            String lecs = String.join(",", m.getLecturerIds());
            tableModel.addRow(new Object[]{m.getId(), m.getName(), m.getAcademicLeaderId(), lecs});
        }
    }

    private void loadModuleFromTable(int row) {
        txtId.setText(tableModel.getValueAt(row, 0).toString());
        txtName.setText(tableModel.getValueAt(row, 1).toString());
        txtLecturers.setText(tableModel.getValueAt(row, 3).toString());
        txtId.setEditable(false);
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtLecturers.setText("");
        txtId.setEditable(true);
        moduleTable.clearSelection();
    }

    private void addModule() {
        String id = txtId.getText();
        if (ModuleManager.getInstance().getModuleById(id) != null) {
            JOptionPane.showMessageDialog(this, "Module ID already exists!");
            return;
        }
        
        Module m = new Module(id, txtName.getText(), currentUser.getId());
        updateLecturers(m);
        ModuleManager.getInstance().addModule(m);
        refreshTable();
        clearForm();
    }

    private void updateModule() {
        String id = txtId.getText();
        ModuleManager.getInstance().deleteModule(id);
        Module m = new Module(id, txtName.getText(), currentUser.getId());
        updateLecturers(m);
        ModuleManager.getInstance().addModule(m);
        refreshTable();
        clearForm();
    }

    private void deleteModule() {
        String id = txtId.getText();
        ModuleManager.getInstance().deleteModule(id);
        refreshTable();
        clearForm();
    }
    
    private void updateLecturers(Module m) {
        String lecs = txtLecturers.getText();
        if (!lecs.isEmpty()) {
            String[] ids = lecs.split(",");
            List<String> validIds = new ArrayList<>();
            List<String> invalidIds = new ArrayList<>();

            for (String lid : ids) {
                lid = lid.trim();
                if (lid.isEmpty()) continue;

                // Verify lecturer exists
                User u = UserManager.getInstance().getUserById(lid);
                if (u != null && u.getRole() == Role.LECTURER) {
                    validIds.add(lid);
                } else {
                    invalidIds.add(lid);
                }
            }

            if (!invalidIds.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Warning: The following Lecturer IDs were not found or are not Lecturers:\n" + String.join(", ", invalidIds),
                    "Invalid Lecturers", JOptionPane.WARNING_MESSAGE);
            }

            m.setLecturerIds(validIds);
        }
    }
}
