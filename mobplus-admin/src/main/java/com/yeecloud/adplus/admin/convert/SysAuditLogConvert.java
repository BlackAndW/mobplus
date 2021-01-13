package com.yeecloud.adplus.admin.convert;

import com.yeecloud.adplus.admin.controller.sys.vo.SysAuditLogVO;
import com.yeecloud.adplus.admin.entity.SysAuditLog;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 操作日志 VO, PO, DTO等转换工具
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Mapper(componentModel = "spring")
public interface SysAuditLogConvert {

    SysAuditLogVO convert(SysAuditLog entity);

    List<SysAuditLogVO> convert(List<SysAuditLog> list);
}
