package cn.sk.api.sys.service.impl;

import cn.sk.api.base.BaseServiceTest;
import cn.sk.api.sys.pojo.SysUser;
import cn.sk.api.sys.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Deseription
 * @Author zhoucp
 * @Date 2020/5/14 10:52
 */
public class SysUserServiceImplTest extends BaseServiceTest {
    @Autowired
    ISysUserService sysUserService;

//    @Test
    public  void insert() {
        SysUser sysUser = new SysUser();
        sysUser.setUserName("用户名test");
        sysUser.setPassword("123456");
        sysUser.setEmail("1156693435");
        sysUserService.insert(sysUser);
    }
//    @Test
    public  void update() {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(36);
        sysUser.setEmail("1156");
        sysUserService.update(sysUser);
    }

}
