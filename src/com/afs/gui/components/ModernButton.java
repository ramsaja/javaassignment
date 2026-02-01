package com.afs.gui.components;

import com.afs.utils.Theme;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.BorderFactory;

public class ModernButton extends JButton {
    private Color hoverColor;
    private Color normalColor;

    public ModernButton(String text) {
        this(text, Theme.SIDEBAR_COLOR, Theme.PRIMARY_COLOR);
    }

    public ModernButton(String text, Color normalColor, Color hoverColor) {
        super(text);
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;

        setFont(Theme.FONT_BUTTON);
        setBackground(normalColor);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor);
            }
        });
    }

    public void setNormalColor(Color color) {
        this.normalColor = color;
        setBackground(color);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(hoverColor.darker());
        } else if (getModel().isRollover()) {
            g.setColor(hoverColor);
        } else {
            g.setColor(getBackground());
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}
