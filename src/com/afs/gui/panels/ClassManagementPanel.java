package com.afs.gui.panels;

import com.afs.gui.components.ModernButton;
import com.afs.gui.components.ModernTextField;
import com.afs.managers.ClassManager;
import com.afs.managers.ModuleManager;
import com.afs.models.Module;
import com.afs.models.ModuleClass;
import com.afs.utils.Theme;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClassManagementPanel extends JPanel {
    private JComboBox<String> cmbModules;
    private JComboBox<String> cmbDays;
    private JComboBox<String> cmbTimes;
    private JTable classTable;
    private DefaultTableModel tableModel;
    private ModernTextField txtId, txtRoom;

    public ClassManagementPanel() {
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

        JLabel lblHeader = new JLabel("Class Details");
        lblHeader.setFont(Theme.FONT_BOLD);
        cardPanel.add(lblHeader, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);

        cmbModules = new JComboBox<>();
        txtId = new ModernTextField();
        
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        cmbDays = new JComboBox<>(days);
        
        String[] times = generateTimeSlots();
        cmbTimes = new JComboBox<>(times);
        
        txtRoom = new ModernTextField();

        formPanel.add(new JLabel("Module:"));
        formPanel.add(cmbModules);
        formPanel.add(new JLabel("Class ID (e.g. CS101-A):"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Day:"));
        formPanel.add(cmbDays);
        formPanel.add(new JLabel("Time:"));
        formPanel.add(cmbTimes);
        formPanel.add(new JLabel("Room:"));
        formPanel.add(txtRoom);
        
        cardPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        ModernButton btnAdd = new ModernButton("Add", Theme.PRIMARY_COLOR, Theme.PRIMARY_COLOR.darker());
        ModernButton btnDelete = new ModernButton("Delete", Theme.DANGER_COLOR, Theme.DANGER_COLOR.darker());

        btnAdd.addActionListener(e -> addClass());
        btnDelete.addActionListener(e -> deleteClass());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Theme.BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        topPanel.add(cardPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Class ID", "Module", "Day", "Time", "Room"};
        tableModel = new DefaultTableModel(columns, 0);
        classTable = new JTable(tableModel);
        Theme.styleTable(classTable);
        add(new JScrollPane(classTable), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        cmbModules.removeAllItems();
        List<Module> modules = ModuleManager.getInstance().getAllModules();
        for (Module m : modules) {
            cmbModules.addItem(m.getId());
        }
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<ModuleClass> classes = ClassManager.getInstance().getAllClasses();
        for (ModuleClass c : classes) {
            tableModel.addRow(new Object[]{c.getId(), c.getModuleId(), c.getDay(), c.getTime(), c.getRoom()});
        }
    }

    private void addClass() {
        String moduleId = (String) cmbModules.getSelectedItem();
        String day = (String) cmbDays.getSelectedItem();
        String time = (String) cmbTimes.getSelectedItem();

        if (moduleId == null || day == null || time == null) return;
        
        ModuleClass cls = new ModuleClass(txtId.getText(), moduleId, day, time, txtRoom.getText());
        ClassManager.getInstance().addClass(cls);
        refreshTable();
        
        txtId.setText("");
        txtRoom.setText("");
    }

    private String[] generateTimeSlots() {
        java.util.List<String> slots = new java.util.ArrayList<>();
        for (int h = 8; h <= 20; h++) {
            slots.add(String.format("%02d:00", h));
            slots.add(String.format("%02d:30", h));
        }
        return slots.toArray(new String[0]);
    }

    private void deleteClass() {
        int row = classTable.getSelectedRow();
        if (row == -1) return;
        
        String id = (String) tableModel.getValueAt(row, 0);
        ClassManager.getInstance().deleteClass(id);
        refreshTable();
    }
}
