package cn.sk.api.business.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 员工信息实体类的扩展类
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
//@AllArgsConstructor
public class EmployeeCustom extends Employee {

    public EmployeeCustom(Long empId, String empNo, Integer orgId, String empName, String sex, String email, String mobilePhone, String headimg, String empDesc, String recordStatus, Date updateTime, Date createTime) {
        super(empId,empNo,orgId,empName,sex,email,mobilePhone,headimg,empDesc,recordStatus,updateTime,createTime);
    }

}