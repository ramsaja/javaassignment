package com.afs.models;

public class Lecturer extends User {
    public Lecturer(String id, String username, String password, String fullName) {
        super(id, username, password, fullName, Role.LECTURER);
    }
}
