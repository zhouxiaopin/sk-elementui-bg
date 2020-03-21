package cn.sk.api.sys.service.impl;

import cn.sk.api.base.service.impl.BaseServiceImpl;
import cn.sk.api.sys.common.ServerResponse;
import cn.sk.api.sys.common.SysConst;
import cn.sk.api.sys.mapper.SysSqlConfMapper;
import cn.sk.api.sys.pojo.*;
import cn.sk.api.sys.service.ISysSqlConfService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 系统sql语句配置业务逻辑接口实现类
 */
@Service
@Slf4j
public class SysSqlConfServiceImpl extends BaseServiceImpl<SysSqlConf, SysSqlConfQueryVo, SysSqlConfMapper> implements ISysSqlConfService {
    @Autowired
    private SysSqlConfMapper sysSqlConfMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ServerResponse<SkPageVo<SysSqlConf>> queryObjsByPage(SysSqlConfQueryVo baseQueryVo) {

        SysDictQueryVo sysDictQueryVo = SysDictQueryVo.newInstance();
        sysDictQueryVo.getIsNoLike().put("dictType",true);
        SysDict condition = sysDictQueryVo.getCdtCustom();
        condition.setDictType(SysConst.Dict.SysSqlConf.STATEMENT_TYPE);
        condition.setRecordStatus(SysConst.RecordStatus.ABLE);


        List<SysDict> sysDicts = sysDictMapper.selectListByQueryVo(sysDictQueryVo);
        //类型
        Map<String,String> statementTypeMap = Maps.newHashMap();
        for(int i = 0,len = sysDicts.size(); i < len; i++) {
            SysDict sysDict = sysDicts.get(i);
            statementTypeMap.put(sysDict.getDictCode(),sysDict.getCodeName());
        }

        //数据封装
//        DataTableVo<SysSqlConfCustom> dataTableVo = super.queryObjsByPage(entityQueryVo);
//        List<SysSqlConfCustom> data = dataTableVo.getData();
        ServerResponse<SkPageVo<SysSqlConf>> pageInfo = super.queryObjsByPage(baseQueryVo);
        List<SysSqlConf> data = pageInfo.getData().getList();
        for(int i = 0,len = data.size(); i < len; i++) {
            SysSqlConf sysSqlConf = data.get(i);
            sysSqlConf.setScType(statementTypeMap.get(sysSqlConf.getScType()));
        }
        return pageInfo;
    }

    @Override
    public ServerResponse queryRealByScCode(String scCode) {
        SysSqlConfQueryVo sysSqlConfQueryVo = SysSqlConfQueryVo.newInstance();

        SysSqlConf sysSqlConf = sysSqlConfQueryVo.getCdtCustom();
        sysSqlConf.setScCode(scCode);
        sysSqlConf.setRecordStatus(SysConst.RecordStatus.ABLE);

        List<SysSqlConf> sysSqlConfs =  sysSqlConfMapper.selectListByQueryVo(sysSqlConfQueryVo);
        if(CollectionUtils.isEmpty(sysSqlConfs)) {
            String sc = sysSqlConf.getScCode();
            log.error("sql语句没有配置:语句码为{}",sc);
            return ServerResponse.createByErrorMessage("sql语句没有配置:语句码为"+sc);
        }
        String sql = sysSqlConfs.get(0).getScStatement();

        try {
            List<Map<String,Object>> data = jdbcTemplate.queryForList(sql);
            return ServerResponse.createBySuccess(data);
        }catch (DataAccessException e){
            String sc = sysSqlConf.getScCode();
            log.error("sql语句没有配置:语句码为{}",sc);
            return ServerResponse.createByErrorMessage("sql语句没有配置:语句码为"+sc);
        }
    }
}
