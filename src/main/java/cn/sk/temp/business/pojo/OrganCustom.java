package cn.sk.temp.business.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 机构信息实体类的扩展类
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
//@AllArgsConstructor
public class OrganCustom extends Organ {


    //父机构名
    private String parentOrgName;

    public OrganCustom(Integer orgId, String orgNo, String orgName, Integer parentId, String orgType, String orgPhone, Integer orgSort, String orgDesc, String recordStatus, Date updateTime, Date createTime) {
        super(orgId, orgNo, orgName, parentId, orgType,orgPhone, orgSort, orgDesc, recordStatus, updateTime, createTime);
    }

}