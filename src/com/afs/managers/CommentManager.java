package com.afs.managers;

import com.afs.data.FileManager;
import com.afs.models.Comment;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CommentManager {
    private static CommentManager instance;
    private List<Comment> comments;
    private final String FILE_NAME = "comments.txt";

    private CommentManager() {
        comments = new ArrayList<>();
        loadComments();
    }

    public static CommentManager getInstance() {
        if (instance == null) {
            instance = new CommentManager();
        }
        return instance;
    }

    private void loadComments() {
        comments.clear();
        List<String> lines = FileManager.readFile(FILE_NAME);
        for (String line : lines) {
            Comment c = Comment.fromFileString(line);
            if (c != null) {
                comments.add(c);
            }
        }
    }

    public void saveComments() {
        List<String> data = comments.stream().map(Comment::toFileString).collect(Collectors.toList());
        FileManager.writeFile(FILE_NAME, data);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        saveComments();
    }

    public List<Comment> getCommentsBetween(String userId1, String userId2) {
        return comments.stream()
                .filter(c -> (c.getSenderId().equals(userId1) && c.getReceiverId().equals(userId2)) ||
                             (c.getSenderId().equals(userId2) && c.getReceiverId().equals(userId1)))
                .sorted(Comparator.comparing(Comment::getTimestamp))
                .collect(Collectors.toList());
    }
}
