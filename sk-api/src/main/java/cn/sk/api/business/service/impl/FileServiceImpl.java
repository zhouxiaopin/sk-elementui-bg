package cn.sk.api.business.service.impl;

import cn.sk.api.business.service.IFileService;
import cn.sk.common.common.ServerResponse;
import cn.sk.api.sys.common.SysConst;
import cn.sk.api.sys.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class FileServiceImpl implements IFileService {

    @Override
    public ServerResponse downTemplateFile(HttpServletResponse response, String realPath) {

//        InputStream inputStream = null;
//        OutputStream outputStream = null;
        String fileName = SysConst.DOWNLOAD_FILE_PREFIX+realPath;
        boolean flag = FileUtils.exportFile(response, fileName);
        if(!flag) {
            log.error("下载失败");
            return ServerResponse.createByErrorMessage("下载失败");
        }
        return ServerResponse.createBySuccess();
    }

//    @Override
//    public void exportExcelData(File file, String[] title, int[] colWidth, List<List<String>> lists) {
//        try {
//            OutputStream os = new FileOutputStream(file);
//            //jxl
//            JxlExcelUtil.writeExcel(os, title, colWidth, lists);
//            //poi
////            PoiExcelUtil.writeExcel(os, title, colWidth, lists);
//            FileUtils.exportFile(response,file,true);
//            log.info("导出excel文件路径：{}",file.getAbsolutePath());
//            log.info("导出excel数据成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("导出excel数据失败");
//        }
//    }

}
