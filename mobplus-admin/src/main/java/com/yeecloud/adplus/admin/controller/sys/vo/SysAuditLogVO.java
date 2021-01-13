package com.yeecloud.adplus.admin.controller.sys.vo;

import com.yeecloud.adplus.admin.common.vo.BaseVO;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.Data;

/**
 * 操作日志
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Data
public class SysAuditLogVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String userName;

    private String action;

    private String actName;

    private String detail;

    private String parameter;

    private String requestUrl;

    private String response;

    private String ipAddr;

    private Integer status;

    private String remark;

    public String getResult() {
        if (StringUtils.containsIgnoreCase(response, "\"code\":2000,")) {
            return "成功";
        } else {
            return "失败";
        }
    }
}