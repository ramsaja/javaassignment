package com.afs.models;

public class Assessment {
    private String id;
    private String title;
    private String type; // e.g., "Quiz", "Assignment"
    private String moduleId;
    private int maxMarks;

    public Assessment(String id, String title, String type, String moduleId, int maxMarks) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.moduleId = moduleId;
        this.maxMarks = maxMarks;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getType() { return type; }
    public String getModuleId() { return moduleId; }
    public int getMaxMarks() { return maxMarks; }

    public String toFileString() {
        return String.join("|", id, title, type, moduleId, String.valueOf(maxMarks));
    }
    
    @Override
    public String toString() {
        return title + " (" + type + ")";
    }
}
