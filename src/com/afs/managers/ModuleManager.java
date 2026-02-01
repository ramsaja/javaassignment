package com.afs.managers;

import com.afs.data.FileManager;
import com.afs.models.Enrollment;
import com.afs.models.Module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private static ModuleManager instance;
    private List<Module> modules;
    private List<Enrollment> enrollments;
    private final String MODULES_FILE = "modules.txt";
    private final String ENROLLMENTS_FILE = "enrollments.txt";

    private ModuleManager() {
        modules = new ArrayList<>();
        enrollments = new ArrayList<>();
        loadModules();
        loadEnrollments();
    }

    public static ModuleManager getInstance() {
        if (instance == null) {
            instance = new ModuleManager();
        }
        return instance;
    }

    private void loadModules() {
        modules.clear();
        List<String> lines = FileManager.readFile(MODULES_FILE);
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 3) {
                String id = parts[0];
                String name = parts[1];
                String leaderId = parts[2];
                Module module = new Module(id, name, leaderId);
                
                if (parts.length > 3 && !parts[3].isEmpty()) {
                    String[] lecs = parts[3].split(",");
                    module.setLecturerIds(new ArrayList<>(Arrays.asList(lecs)));
                }
                modules.add(module);
            }
        }
    }

    private void loadEnrollments() {
        enrollments.clear();
        List<String> lines = FileManager.readFile(ENROLLMENTS_FILE);
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 2) {
                enrollments.add(new Enrollment(parts[0], parts[1]));
            }
        }
    }

    public void saveModules() {
        List<String> data = modules.stream().map(Module::toFileString).collect(Collectors.toList());
        FileManager.writeFile(MODULES_FILE, data);
    }

    public void saveEnrollments() {
        List<String> data = enrollments.stream().map(Enrollment::toFileString).collect(Collectors.toList());
        FileManager.writeFile(ENROLLMENTS_FILE, data);
    }

    public List<Module> getAllModules() {
        return modules;
    }

    public void addModule(Module module) {
        modules.add(module);
        saveModules();
    }
    
    public void deleteModule(String moduleId) {
        modules.removeIf(m -> m.getId().equals(moduleId));
        saveModules();
    }

    public List<Enrollment> getEnrollmentsForStudent(String studentId) {
        return enrollments.stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public boolean isStudentEnrolled(String studentId, String moduleId) {
        return enrollments.stream()
                .anyMatch(e -> e.getStudentId().equals(studentId) && e.getModuleId().equals(moduleId));
    }

    public void enrollStudent(String studentId, String moduleId) {
        if (!isStudentEnrolled(studentId, moduleId)) {
            enrollments.add(new Enrollment(studentId, moduleId));
            saveEnrollments();
        }
    }
    
    public List<Module> getModulesByLeader(String leaderId) {
        return modules.stream().filter(m -> m.getAcademicLeaderId().equals(leaderId)).collect(Collectors.toList());
    }
    
    public List<Module> getModulesByLecturer(String lecturerId) {
        return modules.stream().filter(m -> m.getLecturerIds().contains(lecturerId)).collect(Collectors.toList());
    }
    
    public Module getModuleById(String id) {
        return modules.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Module> getModulesByStudent(String studentId) {
        List<String> moduleIds = enrollments.stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .map(Enrollment::getModuleId)
                .collect(Collectors.toList());
        
        return modules.stream()
                .filter(m -> moduleIds.contains(m.getId()))
                .collect(Collectors.toList());
    }
}
