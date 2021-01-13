package com.yeecloud.adplus.admin.convert;

import com.yeecloud.adplus.admin.controller.sys.form.SysRoleForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysRoleVO;
import com.yeecloud.adplus.admin.entity.SysRole;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 系统角色 VO, PO, DTO等转换工具
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Mapper(componentModel = "spring")
public interface SysRoleConvert {

    SysRoleVO convert(SysRole entity);

    List<SysRoleVO> convert(List<SysRole> list);

    SysRole convert(SysRoleForm form);
}
