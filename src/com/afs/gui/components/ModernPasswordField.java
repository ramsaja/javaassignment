package com.afs.gui.components;

import com.afs.utils.Theme;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPasswordField;

public class ModernPasswordField extends JPasswordField {
    public ModernPasswordField() {
        super();
        setup();
    }
    
    public ModernPasswordField(int columns) {
        super(columns);
        setup();
    }

    private void setup() {
        setFont(Theme.FONT_REGULAR);
        setForeground(Theme.TEXT_COLOR_PRIMARY);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.TEXT_COLOR_SECONDARY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
}
