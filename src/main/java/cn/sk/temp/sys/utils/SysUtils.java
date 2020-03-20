package cn.sk.temp.sys.utils;

import cn.sk.temp.sys.pojo.SysUser;
import org.apache.shiro.SecurityUtils;
import org.springframework.util.ObjectUtils;

public class SysUtils {
    /**
     * 获取登录用户的id
     * @return
     */
    public static Integer getUserId() {
        Integer id = null;
        SysUser sysUserInfo = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if(!ObjectUtils.isEmpty(sysUserInfo)) {
            id = sysUserInfo.getUserId();
        }
        return id;
    }

    /**
     * 获取登录用户
     * @return
     */
    public static SysUser getSysUser() {
        SysUser sysUserInfo = (SysUser) SecurityUtils.getSubject().getPrincipal();
        return sysUserInfo;
    }

}
