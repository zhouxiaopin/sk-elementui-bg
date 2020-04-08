package cn.sk.api.sys.service;

import cn.sk.common.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件业务路径接口
 */
public interface IFileService {
    /**
     * 下载文件
     * @param realPath
     */
    ServerResponse downTemplateFile(HttpServletResponse response, String realPath);
    /**
     *
     * @param base64Strs
     * @param path
     * @return
     */
    ServerResponse imgUpload(String[] base64Strs, String path);
    /**
     *
     * @param files 要上传的多个文件
     * @param path  图片缓存路径
     * @return
     */
    ServerResponse imgUpload(MultipartFile[] files, String path);

//    ServerResponse videoUpload(MultipartFile[] files, String path);
}
