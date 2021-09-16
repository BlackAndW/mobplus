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

    /** 用户名 "必传" */
    private String username;

    /** 版本号 "必传" */
    private String version;

    /** 设备名 */
    private String device;

    /** 操作系统 */
    private String os;

    /** ip地址 */
    private String ip;

    /** 地区 */
    private String area;
    //    private String phone;
    /** 评价内容 */
    private String content;

    /** email */
    private String email;
}
