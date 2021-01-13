package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.sys.vo.SysAuditLogVO;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;

/**
 * 操作日志
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
public interface SysAuditLogService {

    PageInfo<SysAuditLogVO> query(Query query) throws ServiceException;

    SysAuditLogVO findById(Integer id) throws ServiceException;

    void delete(Integer... ids) throws ServiceException;

    void deleteAll() throws ServiceException;
}