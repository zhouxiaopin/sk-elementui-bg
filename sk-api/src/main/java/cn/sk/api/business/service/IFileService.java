package cn.sk.api.business.service;

import cn.sk.api.sys.common.ServerResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件业务逻辑接口
 */
public interface IFileService {
    /**
     * 下载文件
     * @param realPath
     */
    ServerResponse downTemplateFile(HttpServletResponse response, String realPath);

    /**
     * 导出数据
     * @param file
     * @param title
     * @param colWidth
     * @param lists
     */
//    void exportExcelData(File file, String[] title, int[] colWidth, List<List<String>> lists);
}
