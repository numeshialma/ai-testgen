package com.ai.util;

import javax.swing.*;
import java.awt.*;

public class AppStyle {

    // Theme Colors
    public static final Color BACKGROUND_COLOR = new Color(243, 232, 244);
    public static final Color LABEL_COLOR = new Color(75, 54, 95);
    public static final Color BUTTON_COLOR = new Color(167, 123, 195);
    public static final Color BUTTON_HOVER = new Color(142, 101, 163);

    // Fonts
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 20);
    public static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FIELD_FONT = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 14);

    // Apply theme to JFrame
    public static void applyFrameStyle(JFrame frame) {
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // maximize
    }

    // Style buttons
    public static void styleButton(JButton button) {
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addHoverEffect(button);
    }

    // Hover effect
    private static void addHoverEffect(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
            }
        });
    }

    // Style label
    public static void styleLabel(JLabel label, boolean isTitle) {
        label.setForeground(LABEL_COLOR);
        label.setFont(isTitle ? TITLE_FONT : LABEL_FONT);
    }

    // Style field (text, password, combo)
    public static void styleField(JComponent field) {
        field.setFont(FIELD_FONT);
        field.setForeground(LABEL_COLOR);
        if (field instanceof JTextField || field instanceof JPasswordField || field instanceof JComboBox) {
            field.setBackground(Color.WHITE);
        }
    }

    public static JTextField createTextField() {
        JTextField field = new JTextField(30);
        styleField(field);
        return field;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(30);
        styleField(field);
        return field;
    }

    public static JComboBox<String> createDropdown(String[] options) {
        JComboBox<String> dropdown = new JComboBox<>(options);
        styleField(dropdown);
        return dropdown;
    }

    // Apply background and font to any dialog
    public static void applyDialogStyle(JDialog dialog) {
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);
    }

}
