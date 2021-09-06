package com.yeecloud.adplus.admin.controller.cms.vo;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/9/2
 */
@Data
public class UserFeedbackVO {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer appId;
    private String appName;
    private String username;
    private String version;
    private String device;
    private String os;
    private String ip;
    private String area;
    //    private String phone;
    private String content;
    private String email;
    private Integer status;
    private String result;
    private String remark;
}
