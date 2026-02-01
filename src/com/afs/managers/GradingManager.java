package com.afs.managers;

import com.afs.data.FileManager;
import com.afs.models.GradeRule;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GradingManager {
    private static GradingManager instance;
    private List<GradeRule> rules;
    private final String FILE_NAME = "grading.txt";

    private GradingManager() {
        rules = new ArrayList<>();
        loadRules();
        if (rules.isEmpty()) {
            // Defaults
            rules.add(new GradeRule("A", 80, 100));
            rules.add(new GradeRule("B", 70, 79));
            rules.add(new GradeRule("C", 60, 69));
            rules.add(new GradeRule("D", 50, 59));
            rules.add(new GradeRule("F", 0, 49));
            saveRules();
        }
    }

    public static GradingManager getInstance() {
        if (instance == null) instance = new GradingManager();
        return instance;
    }

    private void loadRules() {
        rules.clear();
        List<String> lines = FileManager.readFile(FILE_NAME);
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 3) {
                rules.add(new GradeRule(parts[0], Double.parseDouble(parts[1]), Double.parseDouble(parts[2])));
            }
        }
    }

    public void saveRules() {
        List<String> data = rules.stream().map(GradeRule::toFileString).collect(Collectors.toList());
        FileManager.writeFile(FILE_NAME, data);
    }

    public List<GradeRule> getRules() {
        return rules;
    }

    public void addRule(GradeRule rule) {
        rules.add(rule);
        saveRules();
    }
    
    public void removeRule(int index) {
        if (index >= 0 && index < rules.size()) {
            rules.remove(index);
            saveRules();
        }
    }
    
    public void updateRule(int index, GradeRule rule) {
        if (index >= 0 && index < rules.size()) {
            rules.set(index, rule);
            saveRules();
        }
    }

    public String calculateGrade(double marks) {
        for (GradeRule rule : rules) {
            if (marks >= rule.getMinMark() && marks <= rule.getMaxMark()) {
                return rule.getGrade();
            }
        }
        return "N/A"; // Or some default
    }
}
