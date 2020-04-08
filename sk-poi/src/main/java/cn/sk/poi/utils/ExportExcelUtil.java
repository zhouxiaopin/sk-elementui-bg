package cn.sk.poi.utils;

import cn.sk.poi.anno.ExcelAtrr;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 *@Deseription excel 导出工具类
 *@Author zhoucp
 *@Date 2020/3/21 17:31
 **/
public class ExportExcelUtil<T> {


    private final String TYPE_03 = "1";
    private final String TYPE_07 = "2";
    //默认的数据开始的行索引
    private static final Integer DEFAULT_LOWINDEX = 1;
    //模板文件路径前缀
    private static final String TEMP_PATH_PREFIX = "static/file/excel/";




    /**
     * 通过模板导出数据
     *
     * @param params
     * @param type 导出的文件类型
     * @throws IOException
     */
    public boolean exportExcel(@NonNull ExportParam<T> params,String type) {
        try {
            List<T> objs = params.getData();
            //模板的路径
//            String path = params.getTempPath();
            //模板的输入流
            InputStream tempFileIn = params.getTempFileIn();
            //导出的文件名
            String exportFileName = params.getFileName();
            Integer lowIndex = params.getLowIndex();

//            @Au
            HttpServletResponse response = params.getResponse();

            //03
            Workbook workbook = null;
            if(TYPE_07.equals(type)) {
                workbook = new XSSFWorkbook(Objects.requireNonNull(tempFileIn));
//                workbook = new XSSFWorkbook(Objects.requireNonNull(
//                    ExportExcelUtil.class.getClassLoader().getResourceAsStream(path)));

                //判断是否有后缀
                int isHas = exportFileName.lastIndexOf(".");
                if(-1 == isHas) {
                    exportFileName =exportFileName+".xlsx";
                }

            }else{//默认03
                workbook = new HSSFWorkbook(Objects.requireNonNull(tempFileIn));
//                workbook = new HSSFWorkbook(Objects.requireNonNull(
//                        ExportExcelUtil.class.getClassLoader().getResourceAsStream(path)));

                //判断是否有后缀
                int isHas = exportFileName.lastIndexOf(".");
                if(-1 == isHas) {
                    exportFileName =exportFileName+".xls";
                }
            }

            Sheet sheet = workbook.getSheetAt(0);
            //获取第一行
            Row row1 = sheet.getRow(lowIndex);
            short lastCellNum = row1.getLastCellNum();
            CellStyle[] cellStyle = new CellStyle[lastCellNum];
            //获取列样式
            for (int i = 0, len = cellStyle.length; i < len; i++) {
                cellStyle[i] = row1.getCell(i).getCellStyle();
            }

            //生成数据
            for (int i = 0, len = objs.size(); i < len; i++) {
                //创建行
                Row row = sheet.createRow(i + lowIndex);
                T t = objs.get(i);

                Class<?> clazz = t.getClass();
                Class<?> spClazz = clazz.getSuperclass();
                List<Field> fields =new ArrayList<>();
                Field[] fileds = clazz.getDeclaredFields();
                if(null != fileds) {
                    fields.addAll(Arrays.asList(fileds));
                }
                Field[] suFileds = spClazz.getDeclaredFields();
                if(null != suFileds) {
                    fields.addAll(Arrays.asList(suFileds));
                }

                for (int j = 0; j < lastCellNum; j++) {
                    //创建列
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle[j]);

                    //找出有声明注解cn.sk.poi.anno.ExcelAtrr并outSort=j的属性
                    filedlable:for (int p = 0, length = fields.size(); p < length; p++) {
                        Field field = fields.get(p);
                        ExcelAtrr anno = field.getAnnotation(ExcelAtrr.class);
                        if (null != anno) {
                            int sort = anno.outSort();
                            if (sort == j) {
                                field.setAccessible(true);
                                Object val = field.get(t);
                                cell.setCellValue(null != val?val.toString():null);
                                break filedlable;
                            }
                        }
                    }
                }

            }

            String fileName = URLEncoder.encode(exportFileName, "UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("content-disposition", "attachment;filename=" + fileName);
            response.setHeader("filename", fileName);
            //如果想让浏览器能访问到其他的 响应头的话 需要在服务器上设置 Access-Control-Expose-Headers
            response.setHeader("Access-Control-Expose-Headers", "filename,content-disposition");
            workbook.write(response.getOutputStream());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean exportExcel03(@NonNull ExportParam<T> params) {
        return this.exportExcel(params,TYPE_03);
    }

    public boolean exportExcel07(@NonNull ExportParam<T> params) {
       return this.exportExcel(params,TYPE_07);
    }

    @Builder
    @Getter
    @Setter
    public static class ExportParam<T>{
        private List<T> data;
        //模板输入流
        private InputStream tempFileIn;
//        //模板的路径
//        private String tempPath;
        //导出的文件名
        private String fileName;
        //数据开始的行索引
        private Integer lowIndex;

        private HttpServletResponse response;

//        public String getTempPath() {
//            return null==tempPath&&"".equals(tempPath)?tempPath:TEMP_PATH_PREFIX+tempPath;
//        }

        public Integer getLowIndex() {
            return null == lowIndex?DEFAULT_LOWINDEX:lowIndex;
        }
    }


}
