package cn.sk.temp.sys.service.impl;

import cn.sk.temp.base.service.impl.BaseServiceImpl;
import cn.sk.temp.sys.common.ServerResponse;
import cn.sk.temp.sys.common.SysConst;
import cn.sk.temp.sys.mapper.SysSqlConfMapper;
import cn.sk.temp.sys.pojo.SysDictCustom;
import cn.sk.temp.sys.pojo.SysDictQueryVo;
import cn.sk.temp.sys.pojo.SysSqlConfCustom;
import cn.sk.temp.sys.pojo.SysSqlConfQueryVo;
import cn.sk.temp.sys.service.ISysSqlConfService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 系统sql语句配置业务逻辑接口实现类
 */
@Service
public class SysSqlConfServiceImpl extends BaseServiceImpl<SysSqlConfCustom, SysSqlConfQueryVo> implements ISysSqlConfService {
    @Autowired
    private SysSqlConfMapper sysSqlConfMapper;

    @Override
    public ServerResponse<PageInfo<SysSqlConfCustom>> queryObjsByPage(SysSqlConfQueryVo entityQueryVo) {
        SysDictQueryVo sysDictQueryVo = new SysDictQueryVo();
        SysDictCustom condition = new SysDictCustom();

        sysDictQueryVo.getIsNoLike().put("dictType",true);

        condition.setDictType(SysConst.Dict.SysSqlConf.STATEMENT_TYPE);
        condition.setRecordStatus(SysConst.RecordStatus.ABLE);

        sysDictQueryVo.setSysDictCustom(condition);
        List<SysDictCustom> sysDictCustoms = sysDictMapper.selectListByQueryVo(sysDictQueryVo);
        //类型
        Map<String,String> statementTypeMap = Maps.newHashMap();
        for(int i = 0,len = sysDictCustoms.size(); i < len; i++) {
            SysDictCustom sysDictCustom = sysDictCustoms.get(i);
            statementTypeMap.put(sysDictCustom.getDictCode(),sysDictCustom.getCodeName());
        }

        //数据封装
//        DataTableVo<SysSqlConfCustom> dataTableVo = super.queryObjsByPage(entityQueryVo);
//        List<SysSqlConfCustom> data = dataTableVo.getData();
        ServerResponse<PageInfo<SysSqlConfCustom>> pageInfo = super.queryObjsByPage(entityQueryVo);
        List<SysSqlConfCustom> data = pageInfo.getData().getList();
        for(int i = 0,len = data.size(); i < len; i++) {
            SysSqlConfCustom sysSqlConfCustom = data.get(i);
            sysSqlConfCustom.setScType(statementTypeMap.get(sysSqlConfCustom.getScType()));
        }
        return pageInfo;
    }

}
