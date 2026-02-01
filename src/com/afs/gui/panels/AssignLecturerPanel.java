package com.afs.gui.panels;

import com.afs.gui.components.ModernButton;
import com.afs.managers.TeamManager;
import com.afs.managers.UserManager;
import com.afs.models.Role;
import com.afs.models.User;
import com.afs.utils.Theme;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AssignLecturerPanel extends JPanel {
    private JComboBox<String> cmbLeaders;
    private JComboBox<String> cmbLecturers;
    private JTable teamTable;
    private DefaultTableModel tableModel;

    public AssignLecturerPanel() {
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
        
        JLabel lblHeader = new JLabel("Assign Lecturer to Leader");
        lblHeader.setFont(Theme.FONT_BOLD);
        cardPanel.add(lblHeader, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        
        cmbLeaders = new JComboBox<>();
        cmbLecturers = new JComboBox<>();
        
        cmbLeaders.addActionListener(e -> refreshTable());

        formPanel.add(new JLabel("Academic Leader:"));
        formPanel.add(cmbLeaders);
        formPanel.add(new JLabel("Lecturer:"));
        formPanel.add(cmbLecturers);
        
        cardPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        ModernButton btnAssign = new ModernButton("Assign", Theme.PRIMARY_COLOR, Theme.PRIMARY_COLOR.darker());
        ModernButton btnRemove = new ModernButton("Remove Selected", Theme.DANGER_COLOR, Theme.DANGER_COLOR.darker());

        btnAssign.addActionListener(e -> assign());
        btnRemove.addActionListener(e -> remove());

        buttonPanel.add(btnAssign);
        buttonPanel.add(btnRemove);
        
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Theme.BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        topPanel.add(cardPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Leader ID", "Lecturer ID", "Lecturer Name"};
        tableModel = new DefaultTableModel(columns, 0);
        teamTable = new JTable(tableModel);
        Theme.styleTable(teamTable);
        add(new JScrollPane(teamTable), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        cmbLeaders.removeAllItems();
        List<User> leaders = UserManager.getInstance().getUsersByRole(Role.ACADEMIC_LEADER);
        for (User u : leaders) cmbLeaders.addItem(u.getId());
        
        cmbLecturers.removeAllItems();
        List<User> lecs = UserManager.getInstance().getUsersByRole(Role.LECTURER);
        for (User u : lecs) cmbLecturers.addItem(u.getId());
        
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        String leaderId = (String) cmbLeaders.getSelectedItem();
        if (leaderId == null) return;
        
        List<String> lecIds = TeamManager.getInstance().getLecturersForLeader(leaderId);
        for (String lid : lecIds) {
            User u = UserManager.getInstance().getUserById(lid);
            String name = (u != null) ? u.getFullName() : "Unknown";
            tableModel.addRow(new Object[]{leaderId, lid, name});
        }
    }

    private void assign() {
        String leaderId = (String) cmbLeaders.getSelectedItem();
        String lecId = (String) cmbLecturers.getSelectedItem();
        
        if (leaderId != null && lecId != null) {
            TeamManager.getInstance().assignLecturerToLeader(leaderId, lecId);
            refreshTable();
        }
    }

    private void remove() {
        int row = teamTable.getSelectedRow();
        if (row == -1) return;
        
        String leaderId = (String) tableModel.getValueAt(row, 0);
        String lecId = (String) tableModel.getValueAt(row, 1);
        
        TeamManager.getInstance().removeLecturerFromLeader(leaderId, lecId);
        refreshTable();
    }
}
