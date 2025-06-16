package com.ai;

import com.ai.util.AppStyle;

import javax.swing.*;
import java.awt.*;

public class UIApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UIApp::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Test Suite Creator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Apply global theme and maximize
        AppStyle.applyFrameStyle(frame);

        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 16, 12, 16);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Title
        JLabel title = new JLabel("Test Suite Creator", SwingConstants.CENTER);
        AppStyle.styleLabel(title, true);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        frame.add(title, gbc);
        gbc.gridwidth = 1;

        // Input fields
        JTextField suiteNameField = AppStyle.createTextField();
        JTextField urlField = AppStyle.createTextField();
        JTextField usernameField = AppStyle.createTextField();
        JPasswordField passwordField = AppStyle.createPasswordField();

        JComboBox<String> testTypeDropdown = AppStyle.createDropdown(new String[]{"Web", "Mobile", "API", "Performance"});
        JComboBox<String> osDropdown = AppStyle.createDropdown(new String[]{"Windows", "macOS", "Linux"});
        JComboBox<String> browserDropdown = AppStyle.createDropdown(new String[]{"Chrome", "Firefox", "Edge", "Safari"});
        JComboBox<String> envDropdown = AppStyle.createDropdown(new String[]{"QA", "Staging", "Production"});
        JComboBox<String> priorityDropdown = AppStyle.createDropdown(new String[]{"High", "Medium", "Low"});
        JComboBox<String> execModeDropdown = AppStyle.createDropdown(new String[]{"Manual", "Automated"});
        JComboBox<String> deviceDropdown = AppStyle.createDropdown(new String[]{"Android", "iOS"});

        // Mobile device type label
        JLabel deviceLabel = new JLabel("Mobile Device Type:");
        AppStyle.styleLabel(deviceLabel, false);
        deviceLabel.setVisible(false);
        deviceDropdown.setVisible(false);

        // Add all fields
        addLabeledField(frame, gbc, row++, "Test Suite Name:", suiteNameField);
        addLabeledField(frame, gbc, row++, "Type of Testing:", testTypeDropdown);
        addLabeledField(frame, gbc, row++, "Test URL:", urlField);
        addLabeledField(frame, gbc, row++, "Username:", usernameField);
        addLabeledField(frame, gbc, row++, "Password:", passwordField);
        addLabeledField(frame, gbc, row++, "Operating System:", osDropdown);
        addLabeledField(frame, gbc, row++, "Browser:", browserDropdown);
        addLabeledField(frame, gbc, row++, "Environment:", envDropdown);
        addLabeledField(frame, gbc, row++, "Test Priority:", priorityDropdown);
        addLabeledField(frame, gbc, row++, "Execution Mode:", execModeDropdown);

        // Conditional mobile field
        gbc.gridx = 0;
        gbc.gridy = row;
        frame.add(deviceLabel, gbc);
        gbc.gridx = 1;
        frame.add(deviceDropdown, gbc);
        row++;

        testTypeDropdown.addActionListener(e -> {
            boolean isMobile = "Mobile".equalsIgnoreCase((String) testTypeDropdown.getSelectedItem());
            deviceLabel.setVisible(isMobile);
            deviceDropdown.setVisible(isMobile);
            frame.revalidate();
            frame.repaint();
        });

        // Button
        JButton createButton = new JButton("Create Test Suite");
        AppStyle.styleButton(createButton);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        frame.add(createButton, gbc);

        // Button action
        createButton.addActionListener(e -> {
            String testUrl = urlField.getText().trim();
            if (testUrl.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "⚠️ Please enter a valid URL.");
                return;
            }
            frame.dispose();
            SwingUtilities.invokeLater(() -> new TestCaseDashboard().setVisible(true));
        });

        frame.setVisible(true);
    }

    private static void addLabeledField(JFrame frame, GridBagConstraints gbc, int y, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        AppStyle.styleLabel(label, false);
        AppStyle.styleField(field);

        gbc.gridx = 0;
        gbc.gridy = y;
        frame.add(label, gbc);

        gbc.gridx = 1;
        frame.add(field, gbc);
    }
}
