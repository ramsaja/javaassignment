package com.afs.utils;

import java.awt.Color;
import java.awt.Font;

public class Theme {
    // Colors
    public static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light Grey
    public static final Color SIDEBAR_COLOR = new Color(26, 37, 47); // Deep Navy Blue #1A252F
    public static final Color TEXT_COLOR_PRIMARY = new Color(33, 33, 33);
    public static final Color TEXT_COLOR_SECONDARY = new Color(117, 117, 117);
    public static final Color TEXT_COLOR = TEXT_COLOR_PRIMARY; // Alias
    public static final Color TEXT_COLOR_SIDEBAR = Color.WHITE;
    public static final Color INPUT_BG_COLOR = Color.WHITE;
    
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185); // Blue
    public static final Color ACCENT_COLOR = new Color(46, 204, 113); // Emerald Green
    public static final Color DANGER_COLOR = new Color(231, 76, 60); // Red
    
    // Dashboard Cards
    public static final Color CARD_BLUE = new Color(52, 152, 219);
    public static final Color CARD_GREEN = new Color(46, 204, 113);
    public static final Color CARD_ORANGE = new Color(243, 156, 18);

    // Fonts
    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 24);
    public static final Font FONT_REGULAR = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_MENU = new Font("SansSerif", Font.PLAIN, 16);
    
    public static void styleTable(javax.swing.JTable table) {
        table.setRowHeight(30);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(230, 230, 230));
        
        // Header
        javax.swing.table.JTableHeader header = table.getTableHeader();
        header.setBackground(SIDEBAR_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(FONT_BOLD);
        header.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        header.setPreferredSize(new java.awt.Dimension(0, 40));
    }
}
