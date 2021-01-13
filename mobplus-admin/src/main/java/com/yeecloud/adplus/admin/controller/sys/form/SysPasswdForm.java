package com.yeecloud.adplus.admin.controller.sys.form;

import lombok.Data;

/**
 * Title
 *
 * Date: 2019-11-09 02:18:46
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
public class SysPasswdForm {

    private String oldPasswd;

    private String passwd;

    private String confirmPasswd;

}
