package com.yeecloud.adplus.admin.controller.sys.vo;

import com.yeecloud.adplus.admin.common.vo.BaseVO;
import lombok.Data;

/**
 * 系统角色
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Data
public class SysRoleVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    private String code;

    private String name;

    private Integer sort;

    private Integer status;

    private String remark;
}