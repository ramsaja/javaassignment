package com.afs.gui.panels;

import com.afs.gui.components.ModernButton;
import com.afs.gui.components.ModernTextField;
import com.afs.managers.UserManager;
import com.afs.models.*;
import com.afs.utils.Theme;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserManagementPanel extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private ModernTextField txtId, txtUsername, txtPassword, txtFullName;
    private JComboBox<Role> cmbRole;

    public UserManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top: Form
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblHeader = new JLabel("User Details");
        lblHeader.setFont(Theme.FONT_BOLD);
        cardPanel.add(lblHeader, BorderLayout.NORTH);
        cardPanel.add(Box.createVerticalStrut(10), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        // formPanel.setBorder(BorderFactory.createTitledBorder("User Details")); // Removed

        txtId = new ModernTextField();
        txtUsername = new ModernTextField();
        txtPassword = new ModernTextField();
        txtFullName = new ModernTextField();
        cmbRole = new JComboBox<>(Role.values());

        formPanel.add(new JLabel("ID:"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Username:"));
        formPanel.add(txtUsername);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(txtPassword);
        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(txtFullName);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(cmbRole);
        
        cardPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE); // Inside card
        
        ModernButton btnAdd = new ModernButton("Add", Theme.PRIMARY_COLOR, Theme.PRIMARY_COLOR.darker());
        ModernButton btnUpdate = new ModernButton("Update", Theme.ACCENT_COLOR, Theme.ACCENT_COLOR.darker());
        ModernButton btnDelete = new ModernButton("Delete", Theme.DANGER_COLOR, Theme.DANGER_COLOR.darker());
        ModernButton btnClear = new ModernButton("Clear", Theme.SIDEBAR_COLOR, Theme.SIDEBAR_COLOR.darker());

        btnAdd.addActionListener(e -> addUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnClear.addActionListener(e -> clearForm());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Theme.BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // Spacing below card
        topPanel.add(cardPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Center: Table
        String[] columns = {"ID", "Username", "Password", "Full Name", "Role"};
        tableModel = new DefaultTableModel(columns, 0);
        userTable = new JTable(tableModel);
        Theme.styleTable(userTable);
        
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && userTable.getSelectedRow() != -1) {
                loadUserFromTable(userTable.getSelectedRow());
            }
        });

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<User> users = UserManager.getInstance().getAllUsers();
        for (User u : users) {
            tableModel.addRow(new Object[]{u.getId(), u.getUsername(), u.getPassword(), u.getFullName(), u.getRole()});
        }
    }

    private void loadUserFromTable(int row) {
        txtId.setText(tableModel.getValueAt(row, 0).toString());
        txtUsername.setText(tableModel.getValueAt(row, 1).toString());
        txtPassword.setText(tableModel.getValueAt(row, 2).toString());
        txtFullName.setText(tableModel.getValueAt(row, 3).toString());
        cmbRole.setSelectedItem(tableModel.getValueAt(row, 4));
        txtId.setEditable(false); // ID cannot be changed once set
    }

    private void clearForm() {
        txtId.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtFullName.setText("");
        cmbRole.setSelectedIndex(0);
        txtId.setEditable(true);
        userTable.clearSelection();
    }

    private void addUser() {
        String id = txtId.getText();
        if (UserManager.getInstance().getUserById(id) != null) {
            JOptionPane.showMessageDialog(this, "User ID already exists!");
            return;
        }
        
        User newUser = createUserFromForm();
        UserManager.getInstance().addUser(newUser);
        refreshTable();
        clearForm();
    }

    private void updateUser() {
        String id = txtId.getText();
        User existing = UserManager.getInstance().getUserById(id);
        if (existing != null) {
            // Since we don't have setters for everything in base User class in previous step (only pw and name),
            // and we might want to change roles, we might need to remove and re-add if role changes.
            // But for simplicity, let's just update fields we can, or replace the object.
            // Best approach with simple file persistence: delete old, add new.
            UserManager.getInstance().deleteUser(id);
            UserManager.getInstance().addUser(createUserFromForm());
            refreshTable();
            clearForm();
        }
    }

    private void deleteUser() {
        String id = txtId.getText();
        UserManager.getInstance().deleteUser(id);
        refreshTable();
        clearForm();
    }

    private User createUserFromForm() {
        String id = txtId.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String fullName = txtFullName.getText();
        Role role = (Role) cmbRole.getSelectedItem();

        switch (role) {
            case ADMINISTRATOR: return new Admin(id, username, password, fullName);
            case ACADEMIC_LEADER: return new AcademicLeader(id, username, password, fullName);
            case LECTURER: return new Lecturer(id, username, password, fullName);
            case STUDENT: return new Student(id, username, password, fullName);
            default: return new Student(id, username, password, fullName);
        }
    }
}
