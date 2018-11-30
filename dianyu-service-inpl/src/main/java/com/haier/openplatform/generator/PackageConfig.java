package com.haier.openplatform.generator;

import com.baomidou.mybatisplus.toolkit.StringUtils;

public class PackageConfig
{
  private String parent = "com.baomidou";
  private String moduleName = null;
  private String entity = "entity";
  private String service = "service";
  private String serviceImpl = "service.impl";
  private String mapper = "mapper";
  private String xml = "mapper.xml";
  private String controller = "web";
  
  public String getParent()
  {
    if (StringUtils.isNotEmpty(this.moduleName)) {
      return this.parent + "." + this.moduleName;
    }
    return this.parent;
  }
  
  public void setParent(String parent)
  {
    this.parent = parent;
  }
  
  public String getModuleName()
  {
    return this.moduleName;
  }
  
  public void setModuleName(String moduleName)
  {
    this.moduleName = moduleName;
  }
  
  public String getEntity()
  {
    return this.entity;
  }
  
  public void setEntity(String entity)
  {
    this.entity = entity;
  }
  
  public String getService()
  {
    return this.service;
  }
  
  public void setService(String service)
  {
    this.service = service;
  }
  
  public String getServiceImpl()
  {
    return this.serviceImpl;
  }
  
  public void setServiceImpl(String serviceImpl)
  {
    this.serviceImpl = serviceImpl;
  }
  
  public String getMapper()
  {
    return this.mapper;
  }
  
  public void setMapper(String mapper)
  {
    this.mapper = mapper;
  }
  
  public String getXml()
  {
    return this.xml;
  }
  
  public void setXml(String xml)
  {
    this.xml = xml;
  }
  
  public String getController()
  {
    if (StringUtils.isEmpty(this.controller)) {
      return "web";
    }
    return this.controller;
  }
  
  public void setController(String controller)
  {
    this.controller = controller;
  }
}
