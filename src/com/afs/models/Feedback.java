package com.afs.models;

import java.util.UUID;

public class Feedback {
    private String id;
    private String studentId;
    private String moduleId;
    private int rating; // 1-5
    private String comment;
    private long timestamp;

    public Feedback(String studentId, String moduleId, int rating, String comment) {
        this.id = UUID.randomUUID().toString();
        this.studentId = studentId;
        this.moduleId = moduleId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Constructor for loading from file
    public Feedback(String id, String studentId, String moduleId, int rating, String comment, long timestamp) {
        this.id = id;
        this.studentId = studentId;
        this.moduleId = moduleId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String toFileString() {
        // Escape newlines and pipes in comment
        String safeComment = comment.replace("\n", "\\n").replace("|", "");
        return String.format("%s|%s|%s|%d|%s|%d", id, studentId, moduleId, rating, safeComment, timestamp);
    }

    // Getters
    public String getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getModuleId() { return moduleId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public long getTimestamp() { return timestamp; }
}
