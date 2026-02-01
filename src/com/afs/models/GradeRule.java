package com.afs.models;

public class GradeRule {
    private String grade;
    private double minMark;
    private double maxMark;

    public GradeRule(String grade, double minMark, double maxMark) {
        this.grade = grade;
        this.minMark = minMark;
        this.maxMark = maxMark;
    }

    public String getGrade() { return grade; }
    public double getMinMark() { return minMark; }
    public double getMaxMark() { return maxMark; }
    
    public void setGrade(String grade) { this.grade = grade; }
    public void setMinMark(double minMark) { this.minMark = minMark; }
    public void setMaxMark(double maxMark) { this.maxMark = maxMark; }

    public String toFileString() {
        return String.join("|", grade, String.valueOf(minMark), String.valueOf(maxMark));
    }
}
