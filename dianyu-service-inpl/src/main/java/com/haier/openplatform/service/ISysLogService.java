package com.haier.openplatform.service;

import com.baomidou.mybatisplus.service.IService;
import com.haier.openplatform.bean.SysLog;
import com.haier.openplatform.result.PageInfo;

/**
 *
 * SysLog 表数据服务层接口
 *
 */
public interface ISysLogService extends IService<SysLog> {

    void selectDataGrid(PageInfo pageInfo);

}