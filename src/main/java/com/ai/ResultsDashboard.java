package com.ai;

import com.ai.model.TestCase;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ResultsDashboard {
    private static final String REPORTS_DIR = "test-reports";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    public static void generateReport(List<TestCase> testCases) {
        try {
            // Create reports directory if missing
            Files.createDirectories(Paths.get(REPORTS_DIR));

            // Generate filename with timestamp
            String timestamp = DATE_FORMAT.format(new Date());
            String htmlFile = REPORTS_DIR + "/report_" + timestamp + ".html";

            // Generate HTML content
            String html = buildHtmlReport(testCases, timestamp);

            // Save to file
            try (PrintWriter out = new PrintWriter(htmlFile)) {
                out.println(html);
            }

            // Open in default browser
            openInBrowser(htmlFile);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error generating report: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String buildHtmlReport(List<TestCase> testCases, String timestamp) {
        int passed = (int) testCases.stream().filter(t -> "Passed".equals(t.getStatus())).count();
        int failed = testCases.size() - passed;
        double passRate = testCases.isEmpty() ? 0 : (passed * 100.0) / testCases.size();

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n")
                .append("<html>\n")
                .append("<head>\n")
                .append("    <title>Test Execution Report</title>\n")
                .append("    <style>\n")
                .append("        body { font-family: Arial, sans-serif; margin: 20px; }\n")
                .append("        .header { background: #2c3e50; color: white; padding: 15px; border-radius: 5px; }\n")
                .append("        .summary { background: #f8f9fa; padding: 15px; border-radius: 5px; margin-bottom: 20px; }\n")
                .append("        .passed { background: #d4edda; color: #155724; }\n")
                .append("        .failed { background: #f8d7da; color: #721c24; }\n")
                .append("        .not-run { background: #e2e3e5; color: #383d41; }\n")
                .append("        table { width: 100%; border-collapse: collapse; }\n")
                .append("        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }\n")
                .append("        th { background-color: #343a40; color: white; }\n")
                .append("        .progress-container { width: 100%; background-color: #e9ecef; border-radius: 5px; }\n")
                .append("        .progress-bar { height: 20px; border-radius: 5px; background-color: #28a745; width: ")
                .append(passRate).append("%; }\n")
                .append("    </style>\n")
                .append("</head>\n")
                .append("<body>\n")
                .append("    <div class=\"header\">\n")
                .append("        <h1>Test Execution Report</h1>\n")
                .append("        <p>Generated on: ").append(timestamp).append("</p>\n")
                .append("    </div>\n")
                .append("    \n")
                .append("    <div class=\"summary\">\n")
                .append("        <h2>Summary</h2>\n")
                .append("        <p>Total Tests: ").append(testCases.size()).append("</p>\n")
                .append("        <p>Passed: <span style=\"color:green\">").append(passed).append("</span></p>\n")
                .append("        <p>Failed: <span style=\"color:red\">").append(failed).append("</span></p>\n")
                .append("        <div class=\"progress-container\">\n")
                .append("            <div class=\"progress-bar\"></div>\n")
                .append("        </div>\n")
                .append("        <p>Pass Rate: ").append(String.format("%.1f", passRate)).append("%</p>\n")
                .append("    </div>\n")
                .append("    \n")
                .append("    <h2>Detailed Results</h2>\n")
                .append("    <table>\n")
                .append("        <tr>\n")
                .append("            <th>Test ID</th>\n")
                .append("            <th>Category</th>\n")
                .append("            <th>Scenario</th>\n")
                .append("            <th>Status</th>\n")
                .append("            <th>Execution Time</th>\n")
                .append("        </tr>\n");

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        for (TestCase tc : testCases) {
            String statusClass = tc.getStatus() == null ? "not-run" :
                    tc.getStatus().equals("Passed") ? "passed" : "failed";

            html.append("        <tr class=\"").append(statusClass).append("\">\n")
                    .append("            <td>").append(tc.getId()).append("</td>\n")
                    .append("            <td>").append(tc.getLabel()).append("</td>\n")
                    .append("            <td>").append(tc.getScenario()).append("</td>\n")
                    .append("            <td>").append(tc.getStatus() != null ? tc.getStatus() : "Not Run").append("</td>\n")
                    .append("            <td>").append(timeFormat.format(new Date())).append("</td>\n")
                    .append("        </tr>\n");
        }

        html.append("    </table>\n")
                .append("</body>\n")
                .append("</html>");

        return html.toString();
    }

    private static void openInBrowser(String filePath) {
        try {
            File htmlFile = new File(filePath);
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Failed to open report in browser: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}