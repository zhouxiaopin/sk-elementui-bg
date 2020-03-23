package cn.sk.poi.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelCommonUtil {

//    public final static String DATE_OUTPUT_PATTERNS = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public final static String DATE_OUTPUT_PATTERNS = "yyyy-MM-dd HH:mm:ss";
    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            DATE_OUTPUT_PATTERNS);

    public static Object getCellValue(Cell cell) {
        Object value = null;
        switch (cell.getCellType()){
            case STRING://字符串
                value = cell.getStringCellValue();
                break;
            case BOOLEAN://布尔类型
                value = cell.getBooleanCellValue();
                break;
            case NUMERIC://数字类型（包含日期和普通数字）
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue();
                } else {
                    value = cell.getNumericCellValue();
                }
                break;
            case FORMULA://公式型
                value = cell.getCellFormula();
                break;
            default:
                value = "";
                break;
        }
        return value;

    }


    public static String getCellValueStr(Cell cell) {
        String ret = "";
        if (cell == null) return ret;
        switch (cell.getCellType()) {
            case BLANK:
                ret = "";
                break;
            case BOOLEAN:
                ret = String.valueOf(cell.getBooleanCellValue());
                break;
            case ERROR:
                ret = null;
                break;
            case FORMULA:
                Workbook wb = cell.getSheet().getWorkbook();
                CreationHelper crateHelper = wb.getCreationHelper();
                FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
                ret = getCellValueStr(evaluator.evaluateInCell(cell));
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date theDate = cell.getDateCellValue();
                    ret = simpleDateFormat.format(theDate);
                } else {
                    ret = NumberToTextConverter.toText(cell.getNumericCellValue());
                }
                break;
            case STRING:
                ret = cell.getRichStringCellValue().getString();
                break;
            default:
                ret = "";
        }

        return ret.trim(); // 有必要自行trim
    }
}
