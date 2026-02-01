package com.afs.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Module {
    private String id;
    private String name;
    private String academicLeaderId;
    private List<String> lecturerIds;

    public Module(String id, String name, String academicLeaderId) {
        this.id = id;
        this.name = name;
        this.academicLeaderId = academicLeaderId;
        this.lecturerIds = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAcademicLeaderId() { return academicLeaderId; }
    public List<String> getLecturerIds() { return lecturerIds; }

    public void addLecturer(String lecturerId) {
        if (!lecturerIds.contains(lecturerId)) {
            lecturerIds.add(lecturerId);
        }
    }

    public void removeLecturer(String lecturerId) {
        lecturerIds.remove(lecturerId);
    }

    public void setLecturerIds(List<String> ids) {
        this.lecturerIds = ids;
    }

    public String toFileString() {
        // Format: id|name|leaderId|lec1,lec2,lec3
        String lecs = String.join(",", lecturerIds);
        return String.join("|", id, name, academicLeaderId, lecs);
    }
}
