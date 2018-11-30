package com.haier.openplatform.generator;

import com.baomidou.mybatisplus.generator.AbstractGenerator;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
/**
 * 重写代码生成
 * @author Administrator
 *
 */
public class AutoGenerator extends AbstractGenerator{
  private static final Log logger = LogFactory.getLog(AutoGenerator.class);
  private VelocityEngine engine;
  
  public void execute()
  {
    logger.debug("==========================准备生成文件...==========================");
    
    initConfig();
    
    mkdirs(this.config.getPathInfo());
    
    Map<String, VelocityContext> ctxData = analyzeData(this.config);
    for (Map.Entry<String, VelocityContext> ctx : ctxData.entrySet()) {
      batchOutput((String)ctx.getKey(), (VelocityContext)ctx.getValue());
    }
    if (this.config.getGlobalConfig().isOpen()) {
      try
      {
        String osName = System.getProperty("os.name");
        if (osName != null) {
          if (osName.contains("Mac")) {
            Runtime.getRuntime().exec("open " + this.config.getGlobalConfig().getOutputDir());
          } else if (osName.contains("Windows")) {
            Runtime.getRuntime().exec("cmd /c start " + this.config.getGlobalConfig().getOutputDir());
          } else {
            logger.debug("文件输出目录:" + this.config.getGlobalConfig().getOutputDir());
          }
        }
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    logger.debug("==========================文件生成完成！！！==========================");
  }
  
  private Map<String, VelocityContext> analyzeData(ConfigBuilder config)
  {
    List<TableInfo> tableList = config.getTableInfoList();
    Map<String, String> packageInfo = config.getPackageInfo();
    Map<String, VelocityContext> ctxData = new HashMap();
    String superEntityClass = getSuperClassName(config.getSuperEntityClass());
    String superMapperClass = getSuperClassName(config.getSuperMapperClass());
    String superServiceClass = getSuperClassName(config.getSuperServiceClass());
    String superServiceImplClass = getSuperClassName(config.getSuperServiceImplClass());
    String superControllerClass = getSuperClassName(config.getSuperControllerClass());
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    for (TableInfo tableInfo : tableList)
    {
      VelocityContext ctx = new VelocityContext();
      if (null != this.injectionConfig)
      {
        this.injectionConfig.initMap();
        ctx.put("cfg", this.injectionConfig.getMap());
      }
      if (config.getGlobalConfig().isActiveRecord()) {
        tableInfo.setImportPackages("com.baomidou.mybatisplus.activerecord.Model");
      }
      if (tableInfo.isConvert()) {
        tableInfo.setImportPackages("com.baomidou.mybatisplus.annotations.TableName");
      }
      if (StringUtils.isNotEmpty(config.getSuperEntityClass())) {
        tableInfo.setImportPackages(config.getSuperEntityClass());
      } else {
        tableInfo.setImportPackages("java.io.Serializable");
      }
      ctx.put("package", packageInfo);
      ctx.put("author", config.getGlobalConfig().getAuthor());
      ctx.put("activeRecord", Boolean.valueOf(config.getGlobalConfig().isActiveRecord()));
      ctx.put("date", date);
      ctx.put("table", tableInfo);
      ctx.put("enableCache", Boolean.valueOf(config.getGlobalConfig().isEnableCache()));
      ctx.put("baseResultMap", Boolean.valueOf(config.getGlobalConfig().isBaseResultMap()));
      ctx.put("baseColumnList", Boolean.valueOf(config.getGlobalConfig().isBaseColumnList()));
      ctx.put("entity", tableInfo.getEntityName());
      ctx.put("name", tableInfo.getName());
      ctx.put("entityColumnConstant", Boolean.valueOf(config.getStrategyConfig().isEntityColumnConstant()));
      ctx.put("entityBuilderModel", Boolean.valueOf(config.getStrategyConfig().isEntityBuilderModel()));
      ctx.put("superEntityClass", superEntityClass);
      ctx.put("superMapperClassPackage", config.getSuperMapperClass());
      ctx.put("superMapperClass", superMapperClass);
      ctx.put("superServiceClassPackage", config.getSuperServiceClass());
      ctx.put("superServiceClass", superServiceClass);
      ctx.put("superServiceImplClassPackage", config.getSuperServiceImplClass());
      ctx.put("superServiceImplClass", superServiceImplClass);
      ctx.put("superControllerClassPackage", config.getSuperControllerClass());
      ctx.put("superControllerClass", superControllerClass);
      ctxData.put(tableInfo.getEntityName(), ctx);
    }
    return ctxData;
  }
  
  private String getSuperClassName(String classPath)
  {
    if (StringUtils.isEmpty(classPath)) {
      return null;
    }
    return classPath.substring(classPath.lastIndexOf(".") + 1);
  }
  
  private void mkdirs(Map<String, String> pathInfo)
  {
    for (Map.Entry<String, String> entry : pathInfo.entrySet())
    {
      File dir = new File((String)entry.getValue());
      if (!dir.exists())
      {
        boolean result = dir.mkdirs();
        if (result) {
          logger.debug("创建目录： [" + (String)entry.getValue() + "]");
        }
      }
    }
  }
  
  private void batchOutput(String entityName, VelocityContext context)
  {
    try
    {
      TableInfo tableInfo = (TableInfo)context.get("table");
      Map<String, String> pathInfo = this.config.getPathInfo();
      String entityFile = String.format((String)pathInfo.get("entity_path") + ConstVal.ENTITY_NAME, new Object[] { entityName });
      String mapperFile = String.format((String)pathInfo.get("mapper_path") + File.separator + tableInfo.getMapperName() + ".java", new Object[] { entityName });
      String xmlFile = String.format((String)pathInfo.get("xml_path") + File.separator + tableInfo.getXmlName() + ".xml", new Object[] { entityName });
      String serviceFile = String.format((String)pathInfo.get("serivce_path") + File.separator + tableInfo.getServiceName() + ".java", new Object[] { entityName });
      String implFile = String.format((String)pathInfo.get("serviceimpl_path") + File.separator + tableInfo.getServiceImplName() + ".java", new Object[] { entityName });
      String controllerFile = String.format((String)pathInfo.get("controller_path") + File.separator + tableInfo.getControllerName() + ".java", new Object[] { entityName });
      //生成web页面
      String webFile = String.format((String)pathInfo.get("controller_path")+"web" + File.separator + tableInfo.getControllerName() + ".jsp", new Object[] { entityName });
      
      TemplateConfig template = this.config.getTemplate();
      if (isCreate(entityFile)) {
        vmToFile(context, template.getEntity(), entityFile);
      }
      if (isCreate(mapperFile)) {
        vmToFile(context, template.getMapper(), mapperFile);
      }
      if (isCreate(xmlFile)) {
        vmToFile(context, template.getXml(), xmlFile);
      }
      if (isCreate(serviceFile)) {
        vmToFile(context, template.getService(), serviceFile);
      }
      if (isCreate(implFile)) {
        vmToFile(context, template.getServiceImpl(), implFile);
      }
      if (isCreate(controllerFile)) {
        vmToFile(context, template.getController(), controllerFile);
      }
      //增加页面生成
      if (isCreate(webFile)) {
          vmToFile(context, "/templates/jsp_search_jsp.vm", webFile);
        }
      if (this.injectionConfig != null)
      {
        List<FileOutConfig> focList = this.injectionConfig.getFileOutConfigList();
        if (CollectionUtils.isNotEmpty(focList)) {
          for (FileOutConfig foc : focList) {
            vmToFile(context, foc.getTemplatePath(), foc.outputFile(tableInfo));
          }
        }
      }
    }
    catch (IOException e)
    {
      TableInfo tableInfo;
      logger.error("无法创建文件，请检查配置信息！", e);
    }
  }
  
  private void vmToFile(VelocityContext context, String templatePath, String outputFile)
    throws IOException
  {
    if (StringUtils.isEmpty(templatePath)) {
      return;
    }
    VelocityEngine velocity = getVelocityEngine();
    Template template = velocity.getTemplate(templatePath, ConstVal.UTF8);
    File file = new File(outputFile);
    if (!file.getParentFile().exists()) {
      if (!file.getParentFile().mkdirs())
      {
        logger.debug("创建文件所在的目录失败!");
        return;
      }
    }
    FileOutputStream fos = new FileOutputStream(outputFile);
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, ConstVal.UTF8));
    template.merge(context, writer);
    writer.close();
    logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
  }
  
  private VelocityEngine getVelocityEngine()
  {
    if (this.engine == null)
    {
      Properties p = new Properties();
      p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
      p.setProperty("file.resource.loader.path", "");
      p.setProperty("ISO-8859-1", ConstVal.UTF8);
      p.setProperty("input.encoding", ConstVal.UTF8);
      p.setProperty("output.encoding", ConstVal.UTF8);
      p.setProperty("file.resource.loader.unicode", "true");
      this.engine = new VelocityEngine(p);
    }
    return this.engine;
  }
  
  private boolean isCreate(String filePath)
  {
    File file = new File(filePath);
    return (!file.exists()) || (this.config.getGlobalConfig().isFileOverride());
  }
}
