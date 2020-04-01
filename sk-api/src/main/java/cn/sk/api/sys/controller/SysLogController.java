package cn.sk.api.sys.controller;

import cn.sk.api.base.controller.BaseController;
import cn.sk.api.sys.common.SysConst;
import cn.sk.api.sys.pojo.SysLog;
import cn.sk.api.sys.pojo.SysLogQueryVo;
import cn.sk.api.sys.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统日志 Controller
 */
@RestController
@RequestMapping("/sysLog")
public class SysLogController extends BaseController<SysLog, SysLogQueryVo> {

    @Autowired
    private ISysLogService sysLogService;



    /****************************以下是重新父类的方法*****************************/
    //权限前缀
    @Override
    protected String getPermisPrefix() {
        return SysConst.ShiroPermis.PermisPrefix.SYSlOG;
    }

}
