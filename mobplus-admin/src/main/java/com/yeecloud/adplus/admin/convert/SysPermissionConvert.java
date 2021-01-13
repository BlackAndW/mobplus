package com.yeecloud.adplus.admin.convert;

import com.yeecloud.adplus.admin.controller.sys.form.SysPermissionForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysPermissionVO;
import com.yeecloud.adplus.admin.entity.SysPermission;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 权限信息 VO, PO, DTO等转换工具
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Mapper(componentModel = "spring")
public interface SysPermissionConvert {

    SysPermissionVO convert(SysPermission entity);

    List<SysPermissionVO> convert(List<SysPermission> list);

    SysPermission convert(SysPermissionForm form);
}
