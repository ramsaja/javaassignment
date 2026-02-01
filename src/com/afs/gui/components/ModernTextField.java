package com.afs.gui.components;

import com.afs.utils.Theme;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class ModernTextField extends JTextField {
    public ModernTextField() {
        super();
        setup();
    }
    
    public ModernTextField(int columns) {
        super(columns);
        setup();
    }

    private void setup() {
        setFont(Theme.FONT_REGULAR);
        setForeground(Theme.TEXT_COLOR_PRIMARY);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.TEXT_COLOR_SECONDARY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }
}
