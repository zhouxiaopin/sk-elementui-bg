package cn.sk.api.sys.service;

import cn.sk.api.base.service.IBaseService;
import cn.sk.common.common.ServerResponse;
import cn.sk.api.sys.pojo.SysResource;
import cn.sk.api.sys.pojo.SysResourceQueryVo;

import java.util.List;
import java.util.Map;

/**
 * 系统资源业务逻辑接口
 */
public interface ISysResourceService extends IBaseService<SysResource, SysResourceQueryVo>{
    //根据条件获取树形
    ServerResponse<List<Map<String,Object>>> querySysResourceTree(SysResourceQueryVo sysResourceQueryVo);
    //获取系统目录
    ServerResponse querySysMenu();
}
