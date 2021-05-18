package com.yeecloud.adplus.admin.controller.data.form;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Leonard
 * @create: 2021/5/18
 */
@Data
public class UserAdInfoForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer appId;

    private String userIp;

    private String appPositionCode;

    private String adChannel;

    private String startTimeStr;

    private String endTimeStr;
}
