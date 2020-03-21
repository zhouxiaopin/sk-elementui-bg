package cn.sk.api.sys.service;

import cn.sk.api.base.service.IBaseService;
import cn.sk.api.sys.common.ServerResponse;
import cn.sk.api.sys.pojo.SysPermis;
import cn.sk.api.sys.pojo.SysPermisQueryVo;

import java.util.List;
import java.util.Map;

/**
 * 系统权限业务逻辑接口
 */
public interface ISysPermisService extends IBaseService<SysPermis, SysPermisQueryVo>{
    //根据条件获取树形
    ServerResponse<List<Map<String,Object>>> querySysPermisTree(SysPermisQueryVo sysPermisQueryVo);
    //获取权限
    ServerResponse getPermis();
}
