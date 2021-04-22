package zcs.commons.utils;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {

    public static void main(String[] args) {
        //    模块名称
        String[] modules = {"goshop"};
        //    要生成的表名
        String[] includeTable = {"index_category"};

        for (String module : modules)
            generator(module,false, includeTable);
    }

    public static void generator(String moduleName,boolean fileOverride, String... includeTable) {
//        src/main/java下的包名
        String parent = "mmm." + moduleName;

        //        获取用户当前工作目录
        String projectPath = System.getProperty("user.dir") + "/" + moduleName;
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 数据库表配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.no_change);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setInclude(includeTable);
//        strategy.setSuperEntityClass("zcs.seckill.common.BaseEntity");
//        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
//        strategy.setSuperControllerClass("zcs.seckill.common.BaseController");
        // 写于父类中的公共字段
//        strategy.setSuperEntityColumns("id");
//        strategy.setInclude("seckill".split(","));
//        strategy.setControllerMappingHyphenStyle(true);
//        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());


        // 全局配置
        GlobalConfig gc = new GlobalConfig();

        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("zcs");
//        是否覆盖
        gc.setFileOverride(fileOverride);
        gc.setOpen(false);
        gc.setSwagger2(true);  //实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 包配置
        PackageConfig pc = new PackageConfig();
//        pc.setModuleName("sys");
        pc.setParent(parent);
        pc.setEntity("model");
        pc.setXml("mapper");
        pc.setController("controller");
        mpg.setPackageInfo(pc);
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        try {
            Resource resource = new ClassPathResource("application.yml");
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            dsc.setUrl(properties.getProperty("url"));
            dsc.setDriverName(properties.getProperty("driver-class-name"));
            dsc.setUsername(properties.getProperty("username"));
            dsc.setPassword(properties.getProperty("password"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        mpg.setDataSource(dsc);


        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
//        String templatePath = "/templates/mapper.xml.vm";
//
//        // 自定义输出配置
//        List<FileOutConfig> focList = new ArrayList<>();
//        // 自定义配置会被优先输出
//        focList.add(new FileOutConfig(templatePath) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
//                return projectPath + "/src/main/resources/mapper/"
//                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        //resources路径下
//        templateConfig.setEntity("templates/entity2.java");
//         templateConfig.setService();
//         templateConfig.setController();

//        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);


        mpg.execute();
    }

}
