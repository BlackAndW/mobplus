package com.yeecloud.adplus.gateway.controller.form;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/9/3
 */
@Data
public class UserFeedbackForm {

    private String appId;
    private String username;
    private String version;
    private String device;
    private String os;
    private String ip;
    private String area;
    //    private String phone;
    private String content;
    private String email;
}
