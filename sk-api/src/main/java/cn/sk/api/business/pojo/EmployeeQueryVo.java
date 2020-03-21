package cn.sk.api.business.pojo;

import cn.sk.api.base.pojo.BaseQueryVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 员工信息实体类的包装对象
 */
@Getter
@Setter
public class EmployeeQueryVo extends BaseQueryVo {
    private EmployeeCustom employeeCustom;

}