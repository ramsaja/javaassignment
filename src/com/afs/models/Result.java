package com.afs.models;

public class Result {
    private String studentId;
    private String assessmentId;
    private double marks;
    private String feedback;

    public Result(String studentId, String assessmentId, double marks, String feedback) {
        this.studentId = studentId;
        this.assessmentId = assessmentId;
        this.marks = marks;
        this.feedback = feedback;
    }

    public String getStudentId() { return studentId; }
    public String getAssessmentId() { return assessmentId; }
    public double getMarks() { return marks; }
    public String getFeedback() { return feedback; }

    public void setMarks(double marks) { this.marks = marks; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public String toFileString() {
        // feedback might contain newlines, we should sanitize or escape it. 
        // For simplicity, we'll replace pipes with something else or assume no pipes.
        // Let's replace | with - and newlines with space for now to keep it simple txt.
        String safeFeedback = feedback.replace("|", "-").replace("\n", " ");
        return String.join("|", studentId, assessmentId, String.valueOf(marks), safeFeedback);
    }
}
