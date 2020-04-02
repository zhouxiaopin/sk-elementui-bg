package cn.sk.api.sys.utils;

import cn.sk.api.base.pojo.BaseModel;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 *@Deseription mybatisplus代码生成器
 *@Author zhoucp
 *@Date 2020/3/12 9:34
 **/
public class MpCodeGenerator {
    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {

        // 1.全局配置
        GlobalConfig config = new GlobalConfig();
        //是否支持AR模式
        config
                .setActiveRecord(false)
                //作者
                .setAuthor("zhoucp")
                //生成路径
                .setOutputDir(System.getProperty("user.dir") + "/sk-api/src/main/java")
                //文件覆盖
                .setFileOverride(true)
                //是否打开生成路径
                .setOpen(false)
                .setMapperName("I%sMapper")
                //主键策略
                .setIdType(IdType.AUTO)
                .setBaseResultMap(true)
                .setBaseColumnList(true);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解


        // 2.数据源配置
        DataSourceConfig dbConfig = new DataSourceConfig();
        //设置数据库类型
        dbConfig
                .setDbType(DbType.MYSQL)
                .setUrl("jdbc:mysql://localhost:3306/sk-elementui?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&useSSL=false")
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUsername("root")
                .setPassword("123456");


        // 3.包名策略配置
        PackageConfig pkConfig = new PackageConfig();
        pkConfig.setParent("cn.sk.api.base")
                .setMapper("mapper")
                .setService("service")
                .setController("controller")
                .setEntity("pojo")
                .setXml("mapper");


        // 4.策略配置
        StrategyConfig strategy = new StrategyConfig();
        String tablePrefix = "tb_";
//        String[] tables = new String[]{
//                "tb_sys_dict"
//                ,"tb_sys_permis"
//                ,"tb_sys_resource"
//                ,"tb_sys_role"
//                ,"tb_sys_sql_conf"
//                ,"tb_sys_user"
//                ,"tb_sys_user_role"
//        };
//        String[] tables = new String[]{
//                "tb_sys_role_permis"
//                ,"tb_sys_role_resource"
//        };
        String[] tables = new String[]{
                "tb_sys_dict"
        };
        //全局大写命名
        strategy.setCapitalMode(true)
                .setEntityLombokModel(true)
                //数据库表映射到实体的命名策略
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setRestControllerStyle(true)
                .setSuperEntityClass(BaseModel.class)
                .setSuperMapperClass("cn.sk.api.base.mapper.IBaseMapper")
                .setSuperServiceClass("cn.sk.api.base.service.IBaseService")
                .setSuperServiceImplClass("cn.sk.api.base.service.impl.BaseServiceImpl")
                .setSuperControllerClass("cn.sk.api.base.controller.BaseController")

                //表名前缀
                .setTablePrefix(tablePrefix)
                //生成的表
                .setInclude(tables);
//                .setInclude("tb_sys_dict");

        //5.自定义代码模板
        TemplateConfig templateConfig = new TemplateConfig()
                .setController("genTemplats/controller.java")
                .setService("genTemplats/service.java")
                .setServiceImpl("genTemplats/serviceImpl.java")
                .setMapper("genTemplats/mapper.java")
                .setEntity("genTemplats/entity.java")
                .setXml("genTemplats/mapper.xml");

        //6.自定义属性注入
        InjectionConfig injectionConfig = new InjectionConfig() {
            //自定义属性注入:abc
            //在.ftl(或者是.vm)模板中，通过${cfg.abc}获取属性
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
//                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };
        //配置自定义属性注入

        // 自定义 xxQueryVo.java 生成
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("genTemplats/queryVo.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return config.getOutputDir()+"/cn/sk/api/base/pojo/"+tableInfo.getEntityName()+"QueryVo.java";
            }
        });
        injectionConfig.setFileOutConfigList(focList);





        // 6代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(config)
                .setDataSource(dbConfig)
                .setPackageInfo(pkConfig)
                .setTemplate(templateConfig)
                .setStrategy(strategy)
                .setCfg(injectionConfig);
        autoGenerator.execute();
    }

}
