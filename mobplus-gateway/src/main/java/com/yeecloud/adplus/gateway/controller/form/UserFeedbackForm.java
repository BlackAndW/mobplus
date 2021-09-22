package com.yeecloud.adplus.gateway.controller.form;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/9/3
 */
@Data
public class UserFeedbackForm {

    /** appId "必传" */
    private String appId;

    /** 版本号 "必传" */
    private String version;

    /** 设备名 */
    private String device;

    /** 操作系统 */
    private String os;

    /** 评价内容 */
    private String content;

    /** email */
    private String email;
}
