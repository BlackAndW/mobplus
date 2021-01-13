package com.yeecloud.adplus.admin.controller.sys.vo;

import lombok.Data;

import java.util.List;

/**
 * Title
 * Description:
 * Date: 2019/9/7 17:36
 * Copyright (c) 2019-2099 YYSKYS YeeCloud
 *
 * @author ybbk
 */
@Data
public class SysMenuTreeVO {
    private Integer id;
    private String title;
    private String path;
    private String redirect;
    private String name;
    private String component;
    private String icon;
    private Boolean hidden;
    private Boolean keepAlive;

    private List<SysMenuTreeVO> children;
}
