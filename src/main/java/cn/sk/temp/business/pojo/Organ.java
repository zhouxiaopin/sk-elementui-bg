package cn.sk.temp.business.pojo;

import java.util.Date;

public class Organ {
    private Integer orgId;

    private String orgNo;

    private String orgName;

    private Integer parentId;

    private String orgType;

    private String orgPhone;

    private Integer orgSort;

    private String orgDesc;

    private String recordStatus;

    private Date updateTime;

    private Date createTime;

    public Organ(Integer orgId, String orgNo, String orgName, Integer parentId, String orgType, String orgPhone, Integer orgSort, String orgDesc, String recordStatus, Date updateTime, Date createTime) {
        this.orgId = orgId;
        this.orgNo = orgNo;
        this.orgName = orgName;
        this.parentId = parentId;
        this.orgType = orgType;
        this.orgPhone = orgPhone;
        this.orgSort = orgSort;
        this.orgDesc = orgDesc;
        this.recordStatus = recordStatus;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public Organ() {
        super();
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo == null ? null : orgNo.trim();
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgPhone() {
        return orgPhone;
    }

    public void setOrgPhone(String orgPhone) {
        this.orgPhone = orgPhone == null ? null : orgPhone.trim();
    }

    public Integer getOrgSort() {
        return orgSort;
    }

    public void setOrgSort(Integer orgSort) {
        this.orgSort = orgSort;
    }

    public String getOrgDesc() {
        return orgDesc;
    }

    public void setOrgDesc(String orgDesc) {
        this.orgDesc = orgDesc == null ? null : orgDesc.trim();
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