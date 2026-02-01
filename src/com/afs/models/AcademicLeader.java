package com.afs.models;

public class AcademicLeader extends User {
    public AcademicLeader(String id, String username, String password, String fullName) {
        super(id, username, password, fullName, Role.ACADEMIC_LEADER);
    }
}
