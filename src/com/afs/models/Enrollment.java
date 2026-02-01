package com.afs.models;

public class Enrollment {
    private String studentId;
    private String moduleId;

    public Enrollment(String studentId, String moduleId) {
        this.studentId = studentId;
        this.moduleId = moduleId;
    }

    public String getStudentId() { return studentId; }
    public String getModuleId() { return moduleId; }

    public String toFileString() {
        return studentId + "|" + moduleId;
    }
}
