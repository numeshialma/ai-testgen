package com.ai;

import com.ai.model.TestCase;
import com.ai.util.AppStyle;
import com.ai.util.ExcelHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestCaseDashboard extends JFrame {

    final JTable testCaseTable;
    private final DefaultTableModel tableModel;
    private final List<TestCase> testCases;
    private static final String EXCEL_FILE = "H:/2025/TestRigorLocal/TestRigor_Mini_Version/Test Suite/Login Page - Test Cases.xlsx";
    private JLabel statsLabel;

    public TestCaseDashboard() {
        setTitle("Test Case Dashboard");
        AppStyle.applyFrameStyle(this);
        setLayout(new BorderLayout());

        // Initialize test cases list and load from Excel
        testCases = new ArrayList<>();
        loadTestCases();

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppStyle.BACKGROUND_COLOR);

        JLabel header = new JLabel("📋 Test Cases for Suite", SwingConstants.CENTER);
        AppStyle.styleLabel(header, true);
        headerPanel.add(header, BorderLayout.NORTH);

        statsLabel = new JLabel("", SwingConstants.CENTER);
        updateStats();
        headerPanel.add(statsLabel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Table Setup
        String[] columnNames = {"Test Case ID", "Scenario", "Expected Result", "Status", "Edit", "Delete", "Run"};
        tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 4; // Only allow editing button columns
            }
        };

        testCaseTable = new JTable(tableModel);
        testCaseTable.setRowHeight(30);

        // Configure button columns
        for (int i = 4; i <= 6; i++) {
            testCaseTable.getColumnModel().getColumn(i).setCellRenderer(new ButtonRenderer());
            testCaseTable.getColumnModel().getColumn(i).setCellEditor(new ButtonEditor(new JCheckBox(), this));
        }

        JScrollPane scrollPane = new JScrollPane(testCaseTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(AppStyle.BACKGROUND_COLOR);

        // Add Test Case Button
        JButton addTestCaseBtn = new JButton("➕ Add Test Case");
        addTestCaseBtn.addActionListener(e -> openAddCaseDialog());
        AppStyle.styleButton(addTestCaseBtn);

        // Export to Excel Button
        JButton exportBtn = new JButton("💾 Export to Excel");
        exportBtn.addActionListener(e -> exportToExcel());
        AppStyle.styleButton(exportBtn);

        // Refresh Button
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.addActionListener(e -> refreshTestCases());
        AppStyle.styleButton(refreshBtn);

        // Results Dashboard Button (NEW)
        JButton resultsBtn = new JButton("📊 View Results Dashboard");
        resultsBtn.addActionListener(e -> ResultsDashboard.generateReport(testCases));
        AppStyle.styleButton(resultsBtn);

        //
        JButton runAllBtn = new JButton("▶ Run All Tests");
        runAllBtn.addActionListener(e -> runAllTestCases());
        AppStyle.styleButton(runAllBtn);
        buttonPanel.add(runAllBtn);


        // Add all buttons to panel
        buttonPanel.add(addTestCaseBtn);
        buttonPanel.add(exportBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(resultsBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Populate table with loaded test cases
        refreshTable();
    }

    private void loadTestCases() {
        try {
            testCases.clear();
            testCases.addAll(ExcelHelper.loadFromExcelFile(new File(EXCEL_FILE)));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading test cases: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTestCases() {
        loadTestCases();
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (TestCase testCase : testCases) {
            if (testCase.getId() != null && !testCase.getId().trim().isEmpty()) {
                tableModel.addRow(new Object[]{
                        testCase.getId(),
                        testCase.getScenario(),
                        testCase.getExpectedResult(),
                        testCase.getStatus() != null ? testCase.getStatus() : "Not Run",
                        "✏",
                        "🗑",
                        "▶"
                });
            }
        }
        updateStats();
    }

    private void updateStats() {
        int total = testCases.size();
        int passed = 0;
        int failed = 0;
        int notRun = 0;
        for (TestCase tc : testCases) {
            if (tc.getId() == null || tc.getId().trim().isEmpty()) {
                continue;
            }
            if ("Passed".equals(tc.getStatus())) {
                passed++;
            } else if ("Failed".equals(tc.getStatus())) {
                failed++;
            } else {
                notRun++;
            }
        }

        statsLabel.setText(String.format("Total: %d | Passed: %d | Failed: %d | Not Run: %d",
                total, passed, failed, notRun));
    }

    private void exportToExcel() {
        try {
            testCases.addAll(ExcelHelper.loadFromExcelFile(new File(EXCEL_FILE)));
            JOptionPane.showMessageDialog(this,
                    "Test cases exported successfully to " + EXCEL_FILE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error exporting test cases: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void openAddCaseDialog() {
        new AddTestCaseDialog(this);
    }

    public void addTestCase(TestCase testCase) {
        testCases.add(testCase);
        tableModel.addRow(new Object[]{
                testCase.getId(),
                testCase.getScenario(),
                testCase.getExpectedResult(),
                testCase.getStatus() != null ? testCase.getStatus() : "Not Run",
                "✏",
                "🗑",
                "▶"
        });
        updateStats();
    }

    public void runTestCase(int row) {
        TestCase testCase = testCases.get(row);

        // Simulate test execution (replace with real test logic)
        boolean passed = Math.random() > 0.3; // 70% pass rate for demo

        testCase.setStatus(passed ? "Passed" : "Failed");

        // Update the table
        tableModel.setValueAt(testCase.getStatus(), row, 3);
        updateStats();

        JOptionPane.showMessageDialog(this,
                "Test Case: " + testCase.getId() +
                        "\nStatus: " + (passed ? "✅ Passed" : "❌ Failed") +
                        "\nScenario: " + testCase.getScenario());
    }

    public void deleteTestCase(int row) {
        testCases.remove(row);
        tableModel.removeRow(row);
        updateStats();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TestCaseDashboard().setVisible(true));
    }

    private void runAllTestCases() {
        int passed = 0;
        int total = testCases.size();

        for (int i = 0; i < total; i++) {
            TestCase testCase = testCases.get(i);
            boolean result = Math.random() > 0.3; // Simulate test run

            testCase.setStatus(result ? "Passed" : "Failed");
            tableModel.setValueAt(testCase.getStatus(), i, 3);

            if (result) passed++;

            // Small delay between tests for realism
            try { Thread.sleep(500); }
            catch (InterruptedException ex) { ex.printStackTrace(); }
        }

        updateStats();
        JOptionPane.showMessageDialog(this,
                String.format("Test Run Complete!\nPassed: %d/%d (%.1f%%)",
                        passed, total, (passed*100.0/total)),
                "Results", JOptionPane.INFORMATION_MESSAGE);
    }
}
