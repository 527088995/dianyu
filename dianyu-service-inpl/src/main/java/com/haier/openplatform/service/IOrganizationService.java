package com.haier.openplatform.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.haier.openplatform.bean.Organization;
import com.haier.openplatform.result.Tree;

/**
 *
 * Organization 表数据服务层接口
 *
 */
public interface IOrganizationService extends IService<Organization> {

    List<Tree> selectTree();

    List<Organization> selectTreeGrid();

}