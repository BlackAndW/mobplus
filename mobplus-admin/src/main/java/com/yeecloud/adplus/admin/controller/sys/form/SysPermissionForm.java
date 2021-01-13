package com.yeecloud.adplus.admin.controller.sys.form;

import com.yeecloud.adplus.admin.common.form.BaseForm;
import lombok.Data;

/**
 * 权限信息
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Data
public class SysPermissionForm extends BaseForm {

    private Integer category;

    private String code;

    private String name;

    private String nameEn;

    private String action;

    private Integer sort;

    private Integer status;

    private String remark;
}