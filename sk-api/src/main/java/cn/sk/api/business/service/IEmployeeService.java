package cn.sk.api.business.service;

import cn.sk.api.business.pojo.Employee;
import cn.sk.common.common.ServerResponse;

/**
 * 员工信息业务逻辑接口
 */
public interface IEmployeeService{

    /**
     * 通过主键查找员工
     * @param empId
     * @return
     */
    ServerResponse<Employee> queryEmpByEmpId(Long empId);
}
