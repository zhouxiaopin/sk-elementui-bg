package cn.sk.temp.business.service;

import cn.sk.temp.business.pojo.Employee;
import cn.sk.temp.sys.common.ServerResponse;

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
