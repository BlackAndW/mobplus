package com.yeecloud.adplus.admin;

import com.yeecloud.meeto.common.result.ResultCode;

/**
 * Title
 *
 * Date: 2019-11-06 18:59:30
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
public interface StatusCode extends ResultCode {

    ResultCode NO_AUTH = new ResultCode.DefaultResultCode(4001, "无效会话");
    ResultCode ACCOUNT_NOT_FOUND = new DefaultResultCode(5101, "账号不存在");
    ResultCode INCORRECT_PASSWD = new DefaultResultCode(5102, "密码错误");
    ResultCode ACCOUNT_DENIED = new DefaultResultCode(5103, "账号禁用");
    ResultCode ACCOUNT_LOCKED = new DefaultResultCode(5105, "账号锁定");
    ResultCode KEY_DUPLICATE = new DefaultResultCode(5106, "关键值重复");
}
