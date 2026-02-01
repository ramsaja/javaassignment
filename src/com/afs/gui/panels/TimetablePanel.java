package com.afs.gui.panels;

import com.afs.managers.ClassManager;
import com.afs.managers.ModuleManager;
import com.afs.models.Module;
import com.afs.models.ModuleClass;
import com.afs.models.Role;
import com.afs.models.User;
import com.afs.utils.Theme;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TimetablePanel extends JPanel {
    private JTable classTable;
    private DefaultTableModel tableModel;
    private User currentUser;

    public TimetablePanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Timetable");
        title.setFont(Theme.FONT_TITLE);
        add(title, BorderLayout.NORTH);

        String[] columns = {"Day", "Time", "Module", "Class ID", "Room"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        classTable = new JTable(tableModel);
        classTable.setRowHeight(25);
        add(new JScrollPane(classTable), BorderLayout.CENTER);

        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Module> relevantModules = new ArrayList<>();
        
        if (currentUser.getRole() == Role.STUDENT) {
            relevantModules = ModuleManager.getInstance().getModulesByStudent(currentUser.getId());
        } else if (currentUser.getRole() == Role.LECTURER) {
            relevantModules = ModuleManager.getInstance().getModulesByLecturer(currentUser.getId());
        } else if (currentUser.getRole() == Role.ACADEMIC_LEADER) {
            // Leaders see all classes for modules they manage
            relevantModules = ModuleManager.getInstance().getModulesByLeader(currentUser.getId());
        } else if (currentUser.getRole() == Role.ADMINISTRATOR) {
             relevantModules = ModuleManager.getInstance().getAllModules();
        }

        List<String> moduleIds = relevantModules.stream().map(Module::getId).collect(Collectors.toList());
        List<ModuleClass> allClasses = ClassManager.getInstance().getAllClasses();

        for (ModuleClass c : allClasses) {
            if (moduleIds.contains(c.getModuleId())) {
                Module m = ModuleManager.getInstance().getModuleById(c.getModuleId());
                String moduleName = (m != null) ? m.getName() : c.getModuleId();
                tableModel.addRow(new Object[]{c.getDay(), c.getTime(), moduleName, c.getId(), c.getRoom()});
            }
        }
    }
}
