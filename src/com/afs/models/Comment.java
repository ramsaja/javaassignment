package com.afs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment {
    private String id;
    private String senderId;
    private String receiverId;
    private String content;
    private LocalDateTime timestamp;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Comment(String id, String senderId, String receiverId, String content, LocalDateTime timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getFormattedTimestamp() { return timestamp.format(formatter); }

    public String toFileString() {
        // Sanitize content to avoid breaking the file format
        String safeContent = content.replace("|", "").replace("\n", " ");
        return String.join("|", id, senderId, receiverId, safeContent, getFormattedTimestamp());
    }

    public static Comment fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 5) {
            return new Comment(
                parts[0],
                parts[1],
                parts[2],
                parts[3],
                LocalDateTime.parse(parts[4], formatter)
            );
        }
        return null;
    }
}
