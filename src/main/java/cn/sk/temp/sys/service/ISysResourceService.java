package cn.sk.temp.sys.service;

import cn.sk.temp.base.service.IBaseService;
import cn.sk.temp.sys.common.ServerResponse;
import cn.sk.temp.sys.pojo.SysResourceCustom;
import cn.sk.temp.sys.pojo.SysResourceQueryVo;

import java.util.List;
import java.util.Map;

/**
 * 系统资源业务逻辑接口
 */
public interface ISysResourceService extends IBaseService<SysResourceCustom, SysResourceQueryVo>{
    //根据条件获取树形
    ServerResponse<List<Map<String,Object>>> querySysResourceTree(SysResourceQueryVo sysResourceQueryVo);
    //获取系统目录
    ServerResponse querySysMenu();
}
