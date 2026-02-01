package com.afs.managers;

import com.afs.data.FileManager;
import com.afs.models.ModuleClass;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassManager {
    private static ClassManager instance;
    private List<ModuleClass> classes;
    private final String FILE_NAME = "classes.txt";

    private ClassManager() {
        classes = new ArrayList<>();
        loadClasses();
    }

    public static ClassManager getInstance() {
        if (instance == null) instance = new ClassManager();
        return instance;
    }

    private void loadClasses() {
        classes.clear();
        List<String> lines = FileManager.readFile(FILE_NAME);
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 5) {
                classes.add(new ModuleClass(parts[0], parts[1], parts[2], parts[3], parts[4]));
            }
        }
    }

    public void saveClasses() {
        List<String> data = classes.stream().map(ModuleClass::toFileString).collect(Collectors.toList());
        FileManager.writeFile(FILE_NAME, data);
    }

    public List<ModuleClass> getAllClasses() {
        return classes;
    }
    
    public List<ModuleClass> getClassesForModule(String moduleId) {
        return classes.stream().filter(c -> c.getModuleId().equals(moduleId)).collect(Collectors.toList());
    }

    public void addClass(ModuleClass cls) {
        classes.add(cls);
        saveClasses();
    }
    
    public void deleteClass(String id) {
        classes.removeIf(c -> c.getId().equals(id));
        saveClasses();
    }
}
