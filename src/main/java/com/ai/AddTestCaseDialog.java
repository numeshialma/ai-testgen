package com.ai;

import com.ai.model.TestCase;
import com.ai.util.AppStyle;

import javax.swing.*;
import java.awt.*;

public class AddTestCaseDialog extends JDialog {
    public AddTestCaseDialog(TestCaseDashboard dashboard) {
        setTitle("Add New Test Case");
        setModal(true);
        setSize(400, 300);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(dashboard);

        AppStyle.applyDialogStyle(this);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField(25);
        JTextField scenarioField = new JTextField(25);
        JTextField expectedField = new JTextField(25);
        JTextField labelField = new JTextField(25);

        // Apply consistent field font
        idField.setFont(AppStyle.FIELD_FONT);
        scenarioField.setFont(AppStyle.FIELD_FONT);
        expectedField.setFont(AppStyle.FIELD_FONT);
        labelField.setFont(AppStyle.FIELD_FONT);

        // Row 0 - Test Case ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Test Case ID:"), gbc);
        gbc.gridx = 1;
        add(idField, gbc);

        // Row 1 - Scenario
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Scenario:"), gbc);
        gbc.gridx = 1;
        add(scenarioField, gbc);

        // Row 2 - Expected Result
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Expected Result:"), gbc);
        gbc.gridx = 1;
        add(expectedField, gbc);

        // Row 3 - Label
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Label:"), gbc);
        gbc.gridx = 1;
        add(labelField, gbc);

        // Add and Run Button
        JButton addButton = new JButton("Add and Run");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(addButton, gbc);

        // Button Action
        addButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String scenario = scenarioField.getText().trim();
            String expected = expectedField.getText().trim();
            String label = labelField.getText().trim();

            if (id.isEmpty() || scenario.isEmpty() || expected.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            TestCase testCase = new TestCase(id, scenario, expected, label);
            dashboard.addTestCase(testCase);
            dashboard.runTestCase(dashboard.testCaseTable.getRowCount() - 1);
            dispose();
        });

        setVisible(true);
    }
}
