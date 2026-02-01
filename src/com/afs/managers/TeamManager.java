package com.afs.managers;

import com.afs.data.FileManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TeamManager {
    private static TeamManager instance;
    private Map<String, List<String>> leaderToLecturers; // LeaderID -> List<LecturerID>
    private final String FILE_NAME = "teams.txt";

    private TeamManager() {
        leaderToLecturers = new HashMap<>();
        loadTeams();
    }

    public static TeamManager getInstance() {
        if (instance == null) instance = new TeamManager();
        return instance;
    }

    private void loadTeams() {
        leaderToLecturers.clear();
        List<String> lines = FileManager.readFile(FILE_NAME);
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 2) {
                String leaderId = parts[0];
                String[] lecs = parts[1].split(",");
                List<String> lecList = new ArrayList<>();
                for (String l : lecs) {
                    if (!l.trim().isEmpty()) lecList.add(l.trim());
                }
                leaderToLecturers.put(leaderId, lecList);
            }
        }
    }

    public void saveTeams() {
        List<String> data = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : leaderToLecturers.entrySet()) {
            String lecs = String.join(",", entry.getValue());
            data.add(entry.getKey() + "|" + lecs);
        }
        FileManager.writeFile(FILE_NAME, data);
    }

    public List<String> getLecturersForLeader(String leaderId) {
        return leaderToLecturers.getOrDefault(leaderId, new ArrayList<>());
    }

    public void assignLecturerToLeader(String leaderId, String lecturerId) {
        List<String> lecs = leaderToLecturers.computeIfAbsent(leaderId, k -> new ArrayList<>());
        if (!lecs.contains(lecturerId)) {
            lecs.add(lecturerId);
            saveTeams();
        }
    }
    
    public void removeLecturerFromLeader(String leaderId, String lecturerId) {
        List<String> lecs = leaderToLecturers.get(leaderId);
        if (lecs != null) {
            lecs.remove(lecturerId);
            saveTeams();
        }
    }
}
