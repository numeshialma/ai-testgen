package com.ai.util;

import com.ai.model.TestCase;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExcelHelper {

    // LOAD TEST CASES FROM A FOLDER (All .xlsx files)
    public static List<TestCase> loadFromFolder(String folderPath) throws IOException {
        List<TestCase> allTestCases = new ArrayList<>();
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new IOException("Folder not found: " + folderPath);
        }

        // Process each Excel file in the folder
        for (File file : Objects.requireNonNull(folder.listFiles((dir, name) -> name.endsWith(".xlsx")))) {
            allTestCases.addAll(loadFromExcelFile(file));
        }

        return allTestCases;
    }


    // LOAD TEST CASES FROM A SINGLE EXCEL FILE
    public static List<TestCase> loadFromExcelFile(File excelFile) throws IOException {
        List<TestCase> testCases = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // First sheet

            // Skip header row (row 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Extract data (adjust column indices as needed)
                String category = getCellValue(row.getCell(0)); // Column A (Category)
                String testId = getCellValue(row.getCell(1));   // Column B (Test Case ID)
                String scenario = getCellValue(row.getCell(2));  // Column C (Test Scenario)
                String expected = getCellValue(row.getCell(3));  // Column D (Expected Result)

                // Create TestCase object
                TestCase tc = new TestCase(testId, scenario, expected, category);
                tc.setStatus("Not Run"); // Default status
                testCases.add(tc);
            }
        }

        return testCases;
    }

    // SAVE TEST CASES TO A SINGLE EXCEL FILE
    public static void saveToExcelFile(List<TestCase> testCases, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Test Cases");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Category");
            headerRow.createCell(1).setCellValue("Test Case ID");
            headerRow.createCell(2).setCellValue("Scenario");
            headerRow.createCell(3).setCellValue("Expected Result");
            headerRow.createCell(4).setCellValue("Status");

            // Fill data rows
            for (int i = 0; i < testCases.size(); i++) {
                TestCase tc = testCases.get(i);
                Row row = sheet.createRow(i + 1);

                row.createCell(0).setCellValue(tc.getLabel());      // Category
                row.createCell(1).setCellValue(tc.getId());         // Test Case ID
                row.createCell(2).setCellValue(tc.getScenario());    // Scenario
                row.createCell(3).setCellValue(tc.getExpectedResult()); // Expected Result
                row.createCell(4).setCellValue(tc.getStatus());     // Status
            }

            // Write to file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }

    // HELPER METHOD: Extract cell value safely
    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC: return String.valueOf((int) cell.getNumericCellValue());
            default: return "";
        }
    }

}
