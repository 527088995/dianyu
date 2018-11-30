package com.haier.openplatform.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.haier.openplatform.bean.UserRole;
import com.haier.openplatform.dao.UserRoleMapper;
import com.haier.openplatform.service.IUserRoleService;

/**
 *
 * UserRole 表数据服务层接口实现类
 *
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}