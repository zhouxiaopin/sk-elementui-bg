package cn.sk.temp.business.service;

import java.io.File;
import java.util.List;

/**
 * 文件业务逻辑接口
 */
public interface IFileService {
    /**
     * 下载文件
     * @param realPath
     */
    void downFile(String realPath);

    /**
     * 导出数据
     * @param file
     * @param title
     * @param colWidth
     * @param lists
     */
    void exportExcelData(File file, String[] title, int[] colWidth, List<List<String>> lists);
}
