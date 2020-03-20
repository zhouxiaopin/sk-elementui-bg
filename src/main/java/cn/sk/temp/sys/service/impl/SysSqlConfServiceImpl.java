package cn.sk.temp.sys.service.impl;

import cn.sk.temp.base.service.impl.BaseServiceImpl;
import cn.sk.temp.sys.common.ServerResponse;
import cn.sk.temp.sys.common.SysConst;
import cn.sk.temp.sys.mapper.SysSqlConfMapper;
import cn.sk.temp.sys.pojo.*;
import cn.sk.temp.sys.service.ISysSqlConfService;
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
public class SysSqlConfServiceImpl extends BaseServiceImpl<SysSqlConfCustom, SysSqlConfQueryVo, SysSqlConfMapper> implements ISysSqlConfService {
    @Autowired
    private SysSqlConfMapper sysSqlConfMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ServerResponse<SkPageVo<SysSqlConfCustom>> queryObjsByPage(SysSqlConfQueryVo baseQueryVo) {

        SysDictQueryVo sysDictQueryVo = SysDictQueryVo.newInstance();
        sysDictQueryVo.getIsNoLike().put("dictType",true);
        SysDictCustom condition = sysDictQueryVo.getCdtCustom();
        condition.setDictType(SysConst.Dict.SysSqlConf.STATEMENT_TYPE);
        condition.setRecordStatus(SysConst.RecordStatus.ABLE);


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
        ServerResponse<SkPageVo<SysSqlConfCustom>> pageInfo = super.queryObjsByPage(baseQueryVo);
        List<SysSqlConfCustom> data = pageInfo.getData().getList();
        for(int i = 0,len = data.size(); i < len; i++) {
            SysSqlConfCustom sysSqlConfCustom = data.get(i);
            sysSqlConfCustom.setScType(statementTypeMap.get(sysSqlConfCustom.getScType()));
        }
        return pageInfo;
    }

    @Override
    public ServerResponse queryRealByScCode(String scCode) {
        SysSqlConfQueryVo sysSqlConfQueryVo = SysSqlConfQueryVo.newInstance();

        SysSqlConfCustom sysSqlConfCustom = sysSqlConfQueryVo.getCdtCustom();
        sysSqlConfCustom.setScCode(scCode);
        sysSqlConfCustom.setRecordStatus(SysConst.RecordStatus.ABLE);

        List<SysSqlConfCustom> sysSqlConfCustoms =  sysSqlConfMapper.selectListByQueryVo(sysSqlConfQueryVo);
        if(CollectionUtils.isEmpty(sysSqlConfCustoms)) {
            String sc = sysSqlConfCustom.getScCode();
            log.error("sql语句没有配置:语句码为{}",sc);
            return ServerResponse.createByErrorMessage("sql语句没有配置:语句码为"+sc);
        }
        String sql = sysSqlConfCustoms.get(0).getScStatement();

        try {
            List<Map<String,Object>> data = jdbcTemplate.queryForList(sql);
            return ServerResponse.createBySuccess(data);
        }catch (DataAccessException e){
            String sc = sysSqlConfCustom.getScCode();
            log.error("sql语句没有配置:语句码为{}",sc);
            return ServerResponse.createByErrorMessage("sql语句没有配置:语句码为"+sc);
        }
    }
}
