package com.afs.managers;

import com.afs.data.FileManager;
import com.afs.models.Assessment;
import com.afs.models.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssessmentManager {
    private static AssessmentManager instance;
    private List<Assessment> assessments;
    private List<Result> results;
    private final String ASSESSMENTS_FILE = "assessments.txt";
    private final String RESULTS_FILE = "results.txt";

    private AssessmentManager() {
        assessments = new ArrayList<>();
        results = new ArrayList<>();
        loadAssessments();
        loadResults();
    }

    public static AssessmentManager getInstance() {
        if (instance == null) {
            instance = new AssessmentManager();
        }
        return instance;
    }

    private void loadAssessments() {
        assessments.clear();
        List<String> lines = FileManager.readFile(ASSESSMENTS_FILE);
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 5) {
                assessments.add(new Assessment(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4])));
            }
        }
    }

    private void loadResults() {
        results.clear();
        List<String> lines = FileManager.readFile(RESULTS_FILE);
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 4) {
                results.add(new Result(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3]));
            }
        }
    }

    public void saveAssessments() {
        List<String> data = assessments.stream().map(Assessment::toFileString).collect(Collectors.toList());
        FileManager.writeFile(ASSESSMENTS_FILE, data);
    }

    public void saveResults() {
        List<String> data = results.stream().map(Result::toFileString).collect(Collectors.toList());
        FileManager.writeFile(RESULTS_FILE, data);
    }

    public void addAssessment(Assessment assessment) {
        assessments.add(assessment);
        saveAssessments();
    }

    public List<Assessment> getAssessmentsByModule(String moduleId) {
        return assessments.stream().filter(a -> a.getModuleId().equals(moduleId)).collect(Collectors.toList());
    }

    public Assessment getAssessmentById(String id) {
        return assessments.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    public Result getResult(String studentId, String assessmentId) {
        return results.stream()
                .filter(r -> r.getStudentId().equals(studentId) && r.getAssessmentId().equals(assessmentId))
                .findFirst()
                .orElse(null);
    }

    public void addOrUpdateResult(Result result) {
        Result existing = getResult(result.getStudentId(), result.getAssessmentId());
        if (existing != null) {
            existing.setMarks(result.getMarks());
            existing.setFeedback(result.getFeedback());
        } else {
            results.add(result);
        }
        saveResults();
    }
    
    public List<Result> getResultsByStudent(String studentId) {
        return results.stream().filter(r -> r.getStudentId().equals(studentId)).collect(Collectors.toList());
    }

    public List<Result> getResultsByAssessmentId(String assessmentId) {
        return results.stream().filter(r -> r.getAssessmentId().equals(assessmentId)).collect(Collectors.toList());
    }
}
