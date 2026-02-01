package com.afs.models;

public abstract class User {
    protected String id;
    protected String username;
    protected String password;
    protected String fullName;
    protected Role role;

    public User(String id, String username, String password, String fullName, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public Role getRole() { return role; }

    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    // Helper to format data for text file storage
    public String toFileString() {
        return String.join("|", id, username, password, fullName, role.name());
    }

    @Override
    public String toString() {
        return fullName + " (" + role + ")";
    }
}
