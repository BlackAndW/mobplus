package com.yeecloud.adplus.admin.convert;

import com.yeecloud.adplus.admin.controller.sys.form.SysConfForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysConfVO;
import com.yeecloud.adplus.admin.entity.SysConf;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 系统配置 VO, PO, DTO等转换工具
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Mapper(componentModel = "spring")
public interface SysConfConvert {

    SysConfVO convert(SysConf entity);

    List<SysConfVO> convert(List<SysConf> list);

    SysConf convert(SysConfForm form);
}
