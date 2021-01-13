package com.yeecloud.adplus.admin.convert;

import com.yeecloud.adplus.admin.controller.sys.form.SysMenuForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysMenuVO;
import com.yeecloud.adplus.admin.entity.SysMenu;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 菜单信息 VO, PO, DTO等转换工具
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Mapper(componentModel = "spring")
public interface SysMenuConvert {

    SysMenuVO convert(SysMenu entity);

    List<SysMenuVO> convert(List<SysMenu> list);

    SysMenu convert(SysMenuForm form);
}
