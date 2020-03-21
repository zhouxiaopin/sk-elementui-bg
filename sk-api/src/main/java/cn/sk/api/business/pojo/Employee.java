package cn.sk.api.business.pojo;

import java.util.Date;

public class Employee {
    private Long empId;

    private String empNo;

    private Integer orgId;

    private String empName;

    private String sex;

    private String email;

    private String mobilePhone;

    private String headimg;

    private String empDesc;

    private String recordStatus;

    private Date updateTime;

    private Date createTime;

    public Employee(Long empId, String empNo, Integer orgId, String empName, String sex, String email, String mobilePhone, String headimg, String empDesc, String recordStatus, Date updateTime, Date createTime) {
        this.empId = empId;
        this.empNo = empNo;
        this.orgId = orgId;
        this.empName = empName;
        this.sex = sex;
        this.email = email;
        this.mobilePhone = mobilePhone;
        this.headimg = headimg;
        this.empDesc = empDesc;
        this.recordStatus = recordStatus;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public Employee() {
        super();
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo == null ? null : empNo.trim();
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName == null ? null : empName.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg == null ? null : headimg.trim();
    }

    public String getEmpDesc() {
        return empDesc;
    }

    public void setEmpDesc(String empDesc) {
        this.empDesc = empDesc == null ? null : empDesc.trim();
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus == null ? null : recordStatus.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}