package com.afs.managers;

import com.afs.data.FileManager;
import com.afs.models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserManager {
    private static UserManager instance;
    private List<User> users;
    private final String FILE_NAME = "users.txt";

    private UserManager() {
        users = new ArrayList<>();
        loadUsers();
        // Create default admin if no users exist
        if (users.isEmpty()) {
            users.add(new Admin("admin", "admin", "admin123", "System Administrator"));
            saveUsers();
        }
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void loadUsers() {
        users.clear();
        List<String> lines = FileManager.readFile(FILE_NAME);
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 5) {
                String id = parts[0];
                String username = parts[1];
                String password = parts[2];
                String fullName = parts[3];
                Role role = Role.valueOf(parts[4]);

                User user = null;
                switch (role) {
                    case ADMINISTRATOR: user = new Admin(id, username, password, fullName); break;
                    case ACADEMIC_LEADER: user = new AcademicLeader(id, username, password, fullName); break;
                    case LECTURER: user = new Lecturer(id, username, password, fullName); break;
                    case STUDENT: user = new Student(id, username, password, fullName); break;
                }
                if (user != null) {
                    users.add(user);
                }
            }
        }
    }

    public void saveUsers() {
        List<String> data = users.stream().map(User::toFileString).collect(Collectors.toList());
        FileManager.writeFile(FILE_NAME, data);
    }

    public User authenticate(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    public void updateUser(User user) {
        // Since it's reference based, if we modify the object in list, we just save.
        // But if it's a new object replacing old one, we need to swap.
        // For simplicity, we assume the UI modifies the User object directly.
        saveUsers();
    }

    public void deleteUser(String userId) {
        users.removeIf(u -> u.getId().equals(userId));
        saveUsers();
    }

    public List<User> getAllUsers() {
        return users;
    }

    public List<User> getUsersByRole(Role role) {
        return users.stream().filter(u -> u.getRole() == role).collect(Collectors.toList());
    }

    public User getUserById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }
}
