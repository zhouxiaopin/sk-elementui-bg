package cn.sk.poi.utils;

import cn.sk.poi.anno.ExcelAtrr;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *@Deseription excel 导入工具类
 *@Author zhoucp
 *@Date 2020/3/21 23:46
 **/
public class ImportExcelUtil<T> {
    //默认的数据开始的行索引
    private static final Integer DEFAULT_LOWINDEX = 1;

    public List<T> importExcel(@NonNull ImportParam<T> params) {
        InputStream is = null;
        boolean isO3 = true;
        try {
            is = params.getIs();
            Class<T> clazz = params.getClazz();
            Integer startLowIndex = params.getLowIndex();
            String importFileName = params.getFileName();
            Workbook workbook;
            //判断后缀
            if(importFileName.endsWith(".xlsx")) {//07或者以上
                workbook = new XSSFWorkbook(is);
                isO3 = false;
            }else{
                workbook = new HSSFWorkbook(is);
            }
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            List<T> list = new ArrayList<>();


//            Class clz = this.getClass();//this指的是当前运行的实例（子类实例）
//            ParameterizedType pt = (ParameterizedType)clz.getGenericSuperclass();
//            Class tClass = (Class)pt.getActualTypeArguments()[0];//获取具体的泛型

            //获取泛型
//            Class<?> sptClazz = tClass.getSuperclass();
            Class<?> sptClazz = clazz.getSuperclass();
            List<Field> fields =new ArrayList<>();
            Field[] fileds = clazz.getDeclaredFields();
            if(null != fileds) {
                fields.addAll(Arrays.asList(fileds));
            }
            Field[] suFileds = sptClazz.getDeclaredFields();
            if(null != suFileds) {
                fields.addAll(Arrays.asList(suFileds));
            }


            Map<String, PictureData> picMap = null;
            //判断是否有图片
            if(params.hasPic) {
                picMap = isO3?ExcelCommonUtil.getPicturesByXls(sheet):ExcelCommonUtil.getPicturesByXlsx(sheet);
            }

            for(int i = startLowIndex; i <= lastRowNum; i++) {//行
                Row row = sheet.getRow(i);
                short lastCellNum = row.getLastCellNum();

                //5.创建泛型类的的对象
//                T t = (T) tClass.getConstructor().newInstance();
                T t = clazz.newInstance();

                for(int j = 0; j < lastCellNum; j++) {//列
                    Cell cell = row.getCell(j);
                    Object value = ExcelCommonUtil.getCellValue(cell);

                    //找出有声明注解cn.sk.poi.anno.ExcelAtrr并sort=j的属性
                    filedlable: for (int p = 0, length = fields.size(); p < length; p++) {
                        Field field = fields.get(p);
                        ExcelAtrr anno = field.getAnnotation(ExcelAtrr.class);
                        if (null != anno) {
                            int sort = anno.inSort();
                            if (sort == j) {
                                //zcp------有新类型就在这里加
                                field.setAccessible(true);
                                //获取字段类型
                                Class<?> fieldType= field.getType();
                                //如果是文件类型
                                if(params.hasPic&&PictureData.class == fieldType&& null != picMap
                                        &&picMap.size()>0) {//图片
                                    String picKey = i+"-"+j;
                                    //根据行列获取对应图片
                                    value = picMap.get(picKey);
                                    field.set(t,value);
                                }else{
                                    if(value instanceof Double) {
                                        if(field.getType() == Integer.class) {
                                            field.set(t,((Double) value).intValue());
                                        }
                                    }else{
                                        field.set(t,value);
                                    }
                                }

                                break filedlable;
                            }
                        }
                    }
                }

                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if(null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Builder
    @Getter
    @Setter
    public static class ImportParam<T>{
        private InputStream is;
        private Class clazz;
        //导入的文件名
        private String fileName;
        //数据开始的行索引
        private Integer lowIndex;
        //是否有图片
        private boolean hasPic;
        public Integer getLowIndex() {
            return null == lowIndex?DEFAULT_LOWINDEX:lowIndex;
        }
    }
}
