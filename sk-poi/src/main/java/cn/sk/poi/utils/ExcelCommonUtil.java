package cn.sk.poi.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 获取图片和位置 (xls)
     * @param st
     * @return
     */
    public static Map<String, PictureData> getPicturesByXls (Sheet st) {
        HSSFSheet sheet = (HSSFSheet) st;
        Map<String, PictureData> map = new HashMap<>();
        HSSFPatriarch hssfShapes = sheet.getDrawingPatriarch();
        if(null == hssfShapes) {
            return map;
        }
        List<HSSFShape> list = sheet.getDrawingPatriarch().getChildren();
        for (HSSFShape shape : list) {
            if (shape instanceof HSSFPicture) {
                HSSFPicture picture = (HSSFPicture) shape;
                HSSFClientAnchor cAnchor = (HSSFClientAnchor) picture.getAnchor();
                PictureData pdata = picture.getPictureData();
                String key = cAnchor.getRow1() + "-" + cAnchor.getCol1(); // 行号-列号
                map.put(key, pdata);
            }
        }
        return map;
    }

    /**
     * 获取图片和位置 (xlsx)
     * @param st
     * @return
     */
    public static Map<String, PictureData> getPicturesByXlsx (Sheet st) {
        XSSFSheet sheet = (XSSFSheet) st;
        Map<String, PictureData> map = new HashMap<>();
        List<POIXMLDocumentPart> list = sheet.getRelations();
        for (POIXMLDocumentPart part : list) {
            if (part instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) part;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture picture = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = picture.getPreferredSize();
                    CTMarker marker = anchor.getFrom();
                    String key = marker.getRow() + "-" + marker.getCol();
                    map.put(key, picture.getPictureData());
                }
            }
        }
        return map;
    }
}
