package cn.sk.api.sys.common;

/**
 * 常量类
 */
public class SysConst {

    //下载文件前缀
    public static final String DOWNLOAD_FILE_PREFIX = "static/file/";
    public static final String UPLOAD_FILE_PREFIX = "static/upload/";

    public static String X_ACCESS_TOKEN = "X-Access-Token";
    /** 登录用户Token令牌缓存KEY前缀 */
    public static final String PREFIX_USER_TOKEN  = "prefix_user_token_";

    public interface RecordStatus{
        String DELETE = "00";//记录删除
        String ABLE = "01";//记录可用
        String DISABLE = "02";//记录禁用
    }

    public interface ResponseMsg{
        String OPRT_SUCCE = "操作成功";
        String ADD_SUCCE = "添加成功";
        String UPDATE_SUCCE = "修改成功";
        String DELET_SUCCE = "删除成功";
        String QUERY_SUCCE = "查询成功";

        String OPRT_FAIL = "操作失败";
        String ADD_FAIL = "添加失败";
        String UPDATE_FAIL = "修改失败";
        String DELET_FAIL = "删除失败";
        String QUERY_FAIL = "查询失败";
        String NO_PAGE = "没有配置页面";

        String RECORD_EXISTS_NO = "该记录不存在";

    }

    public interface Dict{
        String RECORDSTATUS_DICTCODE = "record_status";
        //系统权限
        class SysPermis{
            //类型
            public static final String MENU_TYPE = "menu_type";
            //级别
            public static final String MENU_LEVEL = "menu_level";
        }
        //系统资源
        class SysResource{
            //类型
            public static final String RES_TYPE = "res_type";
            //级别
            public static final String RES_LEVEL = "res_level";
            //左边图标
            public static final String LEFT_ICON = "menu_icon";
        }
        //sql语句配置
        class SysSqlConf{
            //类型
            public static final String STATEMENT_TYPE = "statement_type";
        }
    }

    public interface SessionKey{
        String SYSUSER_INFO = "sysUserInfo";
    }

    public interface Permis{
        //权限类型
        String MENU = "01";
        String BUTTON = "02";

        Integer DEFAULT_PARENTID = 0;
    }
    public interface SysResource{
        Integer DEFAULT_PARENTID = 0;
    }
    //shiro权限配置
    public interface ShiroPermis{
        //添加
        String ADD = "add";
        //修改
        String UPDATE = "update";
        //禁用启用
        String UPDATE_RECORDSTATUS = "updateRecordStatus";
        //删除
        String DEL = "del";
        //硬删除
        String REAL_DEL = "realDel";
        //批量删除
        String BATCH_DEL = "batchDel";
        //批量硬删除
        String BATCH_REAL_DEL = "batchRealDel";
        //下载导入模板
        String DOWN_IMPORT_TEMP = "downImportTemp";
        //导出数据
        String EXPORT_DATA = "exportData";
        //批量导入
        String BATCH_IMPORT = "batchImport";


        class PermisPrefix{
            //系统用户
            public static final String SYSUSER = "sysUser:";
            //系统角色
            public static final String SYSROLE = "sysRole:";
            //系统权限
            public static final String SYSPERMIS = "sysPermis:";
            //系统资源
            public static final String SysResource = "sysResource:";
            //系统日志
            public static final String SYSlOG = "sysLog:";
            //数字字典
            public static final String SYSDICT = "sysDict:";
            //sql语句配置
            public static final String SYSSQLCONF = "sysSqlConf:";
        }
    }

}
