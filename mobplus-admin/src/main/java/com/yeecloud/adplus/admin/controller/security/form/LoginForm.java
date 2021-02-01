package com.yeecloud.adplus.admin.controller.security.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Title
 *
 * Date: 2019-11-09 01:51:12
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
public class LoginForm {

    @NotEmpty(message = "用户名不能为空")
    private String userName;
    @NotEmpty(message = "密码不能为空")
    private String passwd;
//    @NotEmpty(message = "验证码不能为空")
    private String captcha;
}
