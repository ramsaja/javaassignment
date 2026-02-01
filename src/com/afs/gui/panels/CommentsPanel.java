package com.afs.gui.panels;

import com.afs.gui.components.ModernButton;
import com.afs.gui.components.ModernTextField;
import com.afs.managers.CommentManager;
import com.afs.managers.ModuleManager;
import com.afs.managers.UserManager;
import com.afs.models.Comment;
import com.afs.models.Module;
import com.afs.models.Role;
import com.afs.models.User;
import com.afs.utils.Theme;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.swing.*;

public class CommentsPanel extends JPanel {
    private User currentUser;
    private JList<User> contactList;
    private DefaultListModel<User> contactListModel;
    private JTextArea chatArea;
    private ModernTextField txtMessage;
    private User selectedContact;

    public CommentsPanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND_COLOR);
        
        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createSidebar(), createChatArea());
        splitPane.setDividerLocation(250);
        splitPane.setBackground(Theme.BACKGROUND_COLOR);
        add(splitPane, BorderLayout.CENTER);

        loadContacts();
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.SIDEBAR_COLOR);
        
        JLabel lblHeader = new JLabel("Contacts");
        lblHeader.setForeground(Theme.TEXT_COLOR_SIDEBAR);
        lblHeader.setFont(Theme.FONT_BOLD);
        lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(lblHeader, BorderLayout.NORTH);

        contactListModel = new DefaultListModel<>();
        contactList = new JList<>(contactListModel);
        contactList.setBackground(Theme.SIDEBAR_COLOR);
        contactList.setForeground(Theme.TEXT_COLOR_SIDEBAR);
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof User) {
                    label.setText(((User) value).getFullName());
                }
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                if (isSelected) {
                    label.setBackground(Theme.PRIMARY_COLOR);
                    label.setOpaque(true);
                } else {
                    label.setBackground(Theme.SIDEBAR_COLOR);
                }
                return label;
            }
        });
        
        contactList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedContact = contactList.getSelectedValue();
                loadChat();
            }
        });

        panel.add(new JScrollPane(contactList), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createChatArea() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BACKGROUND_COLOR);
        
        // Chat History
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chatArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        
        // Input Area
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(Theme.BACKGROUND_COLOR);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        txtMessage = new ModernTextField();
        inputPanel.add(txtMessage, BorderLayout.CENTER);
        
        ModernButton btnSend = new ModernButton("Send", Theme.PRIMARY_COLOR, Theme.PRIMARY_COLOR.darker());
        btnSend.setPreferredSize(new Dimension(80, 35));
        btnSend.addActionListener(e -> sendMessage());
        inputPanel.add(btnSend, BorderLayout.EAST);
        
        panel.add(inputPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private void loadContacts() {
        contactListModel.clear();
        Set<User> contacts = new HashSet<>();

        if (currentUser.getRole() == Role.STUDENT) {
            // Find Lecturers
            List<Module> modules = ModuleManager.getInstance().getModulesByStudent(currentUser.getId());
            for (Module m : modules) {
                for (String lecId : m.getLecturerIds()) {
                    User lec = UserManager.getInstance().getUserById(lecId);
                    if (lec != null) {
                        contacts.add(lec);
                    }
                }
            }
        } else if (currentUser.getRole() == Role.LECTURER) {
            // Find Students
            List<Module> modules = ModuleManager.getInstance().getModulesByLecturer(currentUser.getId());
            List<User> allStudents = UserManager.getInstance().getUsersByRole(Role.STUDENT);
            
            for (User s : allStudents) {
                for (Module m : modules) {
                    if (ModuleManager.getInstance().isStudentEnrolled(s.getId(), m.getId())) {
                        contacts.add(s);
                        break; // Found student in at least one module
                    }
                }
            }
        }

        for (User u : contacts) {
            contactListModel.addElement(u);
        }
    }

    private void loadChat() {
        if (selectedContact == null) return;
        
        chatArea.setText("");
        List<Comment> comments = CommentManager.getInstance().getCommentsBetween(currentUser.getId(), selectedContact.getId());
        
        StringBuilder sb = new StringBuilder();
        sb.append("Conversation with ").append(selectedContact.getFullName()).append("\n\n");
        
        for (Comment c : comments) {
            String senderName = c.getSenderId().equals(currentUser.getId()) ? "You" : selectedContact.getFullName();
            sb.append("[").append(c.getFormattedTimestamp()).append("] ");
            sb.append(senderName).append(": ");
            sb.append(c.getContent()).append("\n\n");
        }
        
        chatArea.setText(sb.toString());
        // Scroll to bottom
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    private void sendMessage() {
        if (selectedContact == null) {
            JOptionPane.showMessageDialog(this, "Please select a contact first.");
            return;
        }
        
        String content = txtMessage.getText().trim();
        if (content.isEmpty()) return;
        
        Comment comment = new Comment(
            UUID.randomUUID().toString(),
            currentUser.getId(),
            selectedContact.getId(),
            content,
            LocalDateTime.now()
        );
        
        CommentManager.getInstance().addComment(comment);
        txtMessage.setText("");
        loadChat();
    }
}
