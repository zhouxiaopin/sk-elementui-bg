package cn.sk.api.business.pojo;

import cn.sk.api.base.pojo.BaseQueryVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 机构信息实体类的包装对象
 */
@Getter
@Setter
public class OrganQueryVo extends BaseQueryVo {
    private OrganCustom organCustom;

}