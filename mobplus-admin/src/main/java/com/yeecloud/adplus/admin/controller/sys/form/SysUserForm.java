package com.yeecloud.adplus.admin.controller.sys.form;

import com.yeecloud.adplus.admin.Constants;
import com.yeecloud.adplus.admin.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
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
public class SysUserForm extends BaseForm {

    @Size(max = 30, min = 4, message = "用户账号应在4~30位之间", groups = Create.class)
    private String userName;

    @NotBlank(message = "密码不能为空", groups = Create.class)
    @Size(max = 40, message = "密码长度不对", groups = Create.class)
    private String passwd;

    private String displayName;

    private String name;

    private Integer gender = 1;

    private String avatar;

    private String email;

    private String mobile;

    private Date birthday;

    private Integer status = Constants.STATE_ON;

    private String remark;

    private List<Integer> roles;
}