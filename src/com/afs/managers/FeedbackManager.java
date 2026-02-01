package com.afs.managers;

import com.afs.data.FileManager;
import com.afs.models.Feedback;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackManager {
    private static FeedbackManager instance;
    private List<Feedback> feedbackList;
    private final String FEEDBACK_FILE = "feedback.txt";

    private FeedbackManager() {
        feedbackList = new ArrayList<>();
        loadFeedback();
    }

    public static FeedbackManager getInstance() {
        if (instance == null) {
            instance = new FeedbackManager();
        }
        return instance;
    }

    private void loadFeedback() {
        feedbackList.clear();
        List<String> lines = FileManager.readFile(FEEDBACK_FILE);
        for (String line : lines) {
            try {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    String comment = parts[4].replace("\\n", "\n");
                    feedbackList.add(new Feedback(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), comment, Long.parseLong(parts[5])));
                }
            } catch (Exception e) {
                // Skip malformed lines
                System.err.println("Error parsing feedback line: " + line);
            }
        }
    }

    public void saveFeedback() {
        List<String> data = feedbackList.stream().map(Feedback::toFileString).collect(Collectors.toList());
        FileManager.writeFile(FEEDBACK_FILE, data);
    }

    public void addFeedback(Feedback feedback) {
        feedbackList.add(feedback);
        saveFeedback();
    }

    public List<Feedback> getFeedbackForModule(String moduleId) {
        return feedbackList.stream().filter(f -> f.getModuleId().equals(moduleId)).collect(Collectors.toList());
    }

    public List<Feedback> getAllFeedback() {
        return new ArrayList<>(feedbackList);
    }
}
