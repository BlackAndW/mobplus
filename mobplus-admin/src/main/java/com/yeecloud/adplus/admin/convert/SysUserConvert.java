package com.yeecloud.adplus.admin.convert;

import com.yeecloud.adplus.admin.Constants;
import com.yeecloud.adplus.admin.controller.sys.form.SysUserForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysUserVO;
import com.yeecloud.adplus.admin.entity.SysUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 系统用户 VO, PO, DTO等转换工具
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Mapper(componentModel = "spring")
public interface SysUserConvert {

    @Mappings({
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "roleCodes", ignore = true),
            @Mapping(source = "birthday", target = "birthday", dateFormat = Constants.DATE_FORMAT_YYYYMMDD)
    })
    SysUserVO convert(SysUser entity);

    List<SysUserVO> convert(List<SysUser> list);

    @Mappings({
            @Mapping(target = "roles", ignore = true),
            @Mapping(source = "birthday", target = "birthday", dateFormat = Constants.DATE_FORMAT_YYYYMMDD)
    })
    SysUser convert(SysUserForm form);
}
