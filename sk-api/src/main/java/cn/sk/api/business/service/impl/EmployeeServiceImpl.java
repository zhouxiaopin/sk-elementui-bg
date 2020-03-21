package cn.sk.api.business.service.impl;

import cn.sk.api.business.common.Const;
import cn.sk.api.business.pojo.Employee;
import cn.sk.api.business.service.IEmployeeService;
import cn.sk.api.sys.common.ServerResponse;
import cn.sk.api.sys.utils.HttpClientUtil;
import cn.sk.api.sys.utils.JackJsonUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 员工信息业务逻辑接口实现类
 */
@Service
@Slf4j
public class EmployeeServiceImpl implements IEmployeeService {

    @Override
    public ServerResponse<Employee> queryEmpByEmpId(Long empId) {
        Map<String,Object> param = Maps.newHashMap();
        param.put("empId",empId);
        JSONObject jsonObject =  HttpClientUtil.sendPostJson("根据员工主键获取员工", Const.Url.Emp.QUERY_EMP_BY_EMPID, JackJsonUtil.obj2String(param));
        String code = jsonObject.getString("code");//接口请求成功0
        String msg = jsonObject.getString("msg");
        Employee employee = jsonObject.getObject("data", Employee.class);
        if(StringUtils.equals("0",code)) {//请求成功
            return ServerResponse.createBySuccess(msg,employee);
        }
        return ServerResponse.createByErrorMessage(msg);
    }
}
