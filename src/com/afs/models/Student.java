package com.afs.models;

public class Student extends User {
    public Student(String id, String username, String password, String fullName) {
        super(id, username, password, fullName, Role.STUDENT);
    }
}
