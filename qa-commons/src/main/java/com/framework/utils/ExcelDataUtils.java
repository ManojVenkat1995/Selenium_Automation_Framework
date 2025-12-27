package com.framework.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelDataUtils {

    public static Object[][] readSheet(String resourcePath, String sheetName) {
        try (InputStream is = ExcelDataUtils.class.getClassLoader()
                .getResourceAsStream(resourcePath);
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheet(sheetName);
            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

            Object[][] data = new Object[rowCount - 1][colCount];

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    data[i - 1][j] = getCellValueAsString(cell);
                }
            }
            return data;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read Excel data from: " + resourcePath, e);
        }
    }

    public static Map<String, String> readRowAsMap(String resourcePath,
                                                   String sheetName,
                                                   int rowIndex) {
        try (InputStream is = ExcelDataUtils.class.getClassLoader()
                .getResourceAsStream(resourcePath);
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);
            Row dataRow = sheet.getRow(rowIndex);

            Map<String, String> map = new HashMap<>();
            int colCount = headerRow.getPhysicalNumberOfCells();

            for (int i = 0; i < colCount; i++) {
                String key = getCellValueAsString(headerRow.getCell(i));
                String value = getCellValueAsString(dataRow.getCell(i));
                map.put(key, value);
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read Excel row as map from: " + resourcePath, e);
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }
}
