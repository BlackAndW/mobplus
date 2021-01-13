package com.yeecloud.adplus.admin.convert;

import com.yeecloud.adplus.admin.controller.sys.form.SysCategoryForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysCategoryVO;
import com.yeecloud.adplus.admin.entity.SysCategory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 数据分类 VO, PO, DTO等转换工具
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Mapper(componentModel = "spring")
public interface SysCategoryConvert {

    SysCategoryVO convert(SysCategory entity);

    List<SysCategoryVO> convert(List<SysCategory> list);

    SysCategory convert(SysCategoryForm form);
}
