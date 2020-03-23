package cn.sk.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * ftp工具类
 */
@Slf4j
@Component
public class FTPUtil {

/*    @Value("${us.ftp.ip}")
    private static String ftpIp = Const.FtpConf.ftpIp;
//    private static String ftpIp = "192.168.239.211";
    @Value("${us.ftp.port}")
    private static int ftpPort = 21;
    @Value("${us.ftp.user}")
    private static String ftpUser = "ftpuser";
    @Value("${us.ftp.pass}")
    private static String ftpPass = "123456";*/

//    @Value("${us.ftp.ip}")
    private String ip;
//    @Value("${us.ftp.port}")
    private int port;
//    @Value("${us.ftp.user}")
    private String user;
//    @Value("${us.ftp.pass}")
    private String pwd;

    private FTPClient ftpClient;
//
//    public FTPUtil(String ip,int port,String user,String pwd){
//        this.ip = ip;
//        this.port = port;
//        this.user = user;
//        this.pwd = pwd;
//    }
//
//    public static boolean uploadFile(List<File> fileList,String remotePath) throws IOException {
//        FTPUtil ftpUtil = new FTPUtil(ftpIp,ftpPort,ftpUser,ftpPass);
//        boolean result = ftpUtil.uploadFile(remotePath,fileList);
//        return result;
//    }


    public boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
        log.info("开始连接ftp服务器");
        boolean uploaded = true;
        FileInputStream fis = null;
        //连接FTP服务器
        if(connectServer(this.ip,this.port,this.user,this.pwd)){
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for(File fileItem : fileList){
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fis);
                }

            } catch (IOException e) {
                log.error("上传文件异常",e);
                uploaded = false;
                e.printStackTrace();
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
        }else{
            uploaded = false;
        }
        log.info("开始连接ftp服务器,结束上传,上传结果:{}",uploaded);
        return uploaded;
    }



    private boolean connectServer(String ip,int port,String user,String pwd){

        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip,port);
            isSuccess = ftpClient.login(user,pwd);
        } catch (IOException e) {
            log.error("连接FTP服务器异常",e);
        }
        return isSuccess;
    }

}
