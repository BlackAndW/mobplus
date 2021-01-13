package com.yeecloud.adplus.admin.controller.sys.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.yeecloud.adplus.admin.common.vo.BaseVO;
import lombok.Data;

import java.util.List;

/**
 * 系统用户
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Data
public class SysUserVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String displayName;

    private String name;

    private Integer gender;

    private String avatar;

    private String email;

    private String mobile;

    private String birthday;

    private Integer status;

    private String remark;

    private List<Integer> roles = Lists.newArrayList();

    private List<String> roleCodes = Lists.newArrayList();

    /*====================*/
    @JSONField(serialize = false)
    private transient String passwd;

    @JSONField(serialize = false)
    private transient String salt;
}