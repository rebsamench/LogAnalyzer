package ch.zhaw.jv19.loganalyzer.util.export;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

public class ExcelExporter {

    public void saveTable(TableView<ObservableList> table, File file) {
        String filepath = file.getAbsolutePath();
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Export");
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            CellStyle evenRowStyle = workbook.createCellStyle();
            evenRowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            evenRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            CellStyle oddRowStyle = workbook.createCellStyle();
            oddRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            oddRowStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setFontHeightInPoints((short) 11);
            font.setBold(true);
            headerStyle.setFont(font);
            //header
            Row header = sheet.createRow(0);
            for (int i = 0; i < table.getColumns().size(); i++) {
                sheet.autoSizeColumn(i);
                Cell headerCell = header.createCell(i);
                headerCell.setCellValue(table.getColumns().get(i).getText());
                headerCell.setCellStyle(headerStyle);
            }

            //content (start with 1 since header is 0)
            for (int i = 0; i < table.getItems().size(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < table.getColumns().size(); j++) {
                    if (table.getColumns().get(j).getCellData(i) != null) {
                        Cell cell = row.createCell(j);
                        //set even columns color to grey
                        if(i % 2 == 0) {
                            cell.setCellStyle(evenRowStyle);
                        } else {
                            cell.setCellStyle(oddRowStyle);
                        }
                        cell.setCellValue(table.getColumns().get(j).getCellData(i).toString());
                    } else {
                        row.createCell(j).setCellValue("");
                    }
                }
            }
            FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
