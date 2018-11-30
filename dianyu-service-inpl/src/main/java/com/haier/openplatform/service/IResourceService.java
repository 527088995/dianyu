package com.haier.openplatform.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.haier.openplatform.bean.Resource;
import com.haier.openplatform.bean.ShiroUser;
import com.haier.openplatform.result.Tree;

/**
 *
 * Resource 表数据服务层接口
 *
 */
public interface IResourceService extends IService<Resource> {

    List<Resource> selectAll();

    List<Tree> selectAllMenu();

    List<Tree> selectAllTree();

    List<Tree> selectTree(ShiroUser shiroUser);

}