package cn.sk.temp.business.service.impl;

import cn.sk.temp.business.service.IFileService;
import cn.sk.temp.sys.common.SysConst;
import cn.sk.temp.sys.utils.FileUtils;
import cn.sk.temp.sys.utils.JxlExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Slf4j
@Service
public class FileServiceImpl implements IFileService {
    @Autowired
    private HttpServletResponse response;

    @Override
    public void downFile(String realPath) {

//        InputStream inputStream = null;
//        OutputStream outputStream = null;
        try {
            String resourceLocation = "classpath:"+ SysConst.DOWNLOAD_FILE_PREFIX+realPath;
            if (ResourceUtils.isUrl(resourceLocation)) {
                //获取文件
                File file = ResourceUtils.getFile(resourceLocation);
                FileUtils.exportFile(response, file, false);
//                //文件扩展名
//                String fileExtensionName = realPath.substring(realPath.lastIndexOf(".")+1);
//                // 文件的默认保存名
//                String fileName = UUID.randomUUID().toString()+"."+fileExtensionName;
//                // 读到流中
//                inputStream = new FileInputStream(file);
//                // 设置输出的格式
//                response.reset();
//                response.setContentType("bin");
//                response.addHeader("Content-Disposition",
//                        "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
//                // 循环取出流中的数据
//                byte[] b = new byte[200];
//                int len;
//                outputStream = response.getOutputStream();
//                while ((len = inputStream.read(b)) != -1){
//                    outputStream.write(b, 0, len);
//                }

            }else{
                log.error("路径不存在");
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error("下载失败");
        }
//        finally {
//            if (outputStream != null) {
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    @Override
    public void exportExcelData(File file, String[] title, int[] colWidth, List<List<String>> lists) {
        try {
            OutputStream os = new FileOutputStream(file);
            //jxl
            JxlExcelUtil.writeExcel(os, title, colWidth, lists);
            //poi
//            PoiExcelUtil.writeExcel(os, title, colWidth, lists);
            FileUtils.exportFile(response,file,true);
            log.info("导出excel文件路径：{}",file.getAbsolutePath());
            log.info("导出excel数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("导出excel数据失败");
        }
    }

}
