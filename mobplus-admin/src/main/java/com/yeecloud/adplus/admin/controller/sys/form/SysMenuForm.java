package com.yeecloud.adplus.admin.controller.sys.form;

import com.yeecloud.adplus.admin.common.form.BaseForm;
import lombok.Data;

/**
 * 菜单信息
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Data
public class SysMenuForm extends BaseForm {

    private String title;

    private String titleEn;

    private String name;

    private String icon;

    private String path;

    private String component;

    private String redirect;

    private Boolean keepAlive = false;

    private Boolean hidden = false;

    private Integer status = 2;

    private Integer sort;

    private String remark;

}