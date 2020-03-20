package cn.sk.temp.sys.utils;

import cn.sk.temp.sys.common.SysConst;
import cn.sk.temp.sys.pojo.SysLog;
import cn.sk.temp.sys.pojo.SysUser;
import cn.sk.temp.sys.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class LogUtil {
    @Autowired
    private ISysLogService sysLogService;

    public void writLog(Class clazz,String methodName,String oprt) {
        this.writLog(clazz, methodName, oprt, true);
    }
    public void writLog(Class clazz,String methodName,String oprt,boolean saveParams) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.debug("======系统日志处理开始======");
        //保存日志
        SysLog sysLog = new SysLog();

        //获取请求的类名
//        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
//        String methodName = method.getName();
//        sysLogCustom.setMethodName(className + "." + methodName);
        String simpleName = clazz.getSimpleName();
        sysLog.setMethodName(simpleName + "_" + methodName);
        sysLog.setExpan1(clazz.getSimpleName() + "_" +methodName);
        //url
        sysLog.setRequestUrl(request.getRequestURL().toString());
        //method
        sysLog.setRequestType(request.getMethod());

        //操作
        sysLog.setOperation(oprt);

        if(saveParams) {
            //请求的参数
            //将参数所在的数组转换成json
            String params = JackJsonUtil.obj2String(request.getParameterMap());
            sysLog.setParams(params);
        }

        //获取用户名
        SysUser sysUserInfo = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if(!ObjectUtils.isEmpty(sysUserInfo)) {
            sysLog.setUserId(sysUserInfo.getUserId());
            sysLog.setUserName(sysUserInfo.getUserName());
        }
        //获取用户ip地址
        sysLog.setIp(IpAdrressUtil.getIpAdrress(request));

        sysLog.setRecordStatus(SysConst.RecordStatus.ABLE);
        //调用service保存SysLog实体类到数据库
        sysLogService.insert(sysLog);

        log.debug("======系统日志处理======");
    }
}
