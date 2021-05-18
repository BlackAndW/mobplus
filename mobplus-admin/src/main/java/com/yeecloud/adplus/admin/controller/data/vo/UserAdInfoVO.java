package com.yeecloud.adplus.admin.controller.data.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Leonard
 * @create: 2021/5/18
 */
@Data
public class UserAdInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userIp;

    private String appId;

    private String appPositionCode;

    private String adChannel;

    private Integer adRequestCount = 0;

    private Integer adShowCount = 0;

    private Integer adClickCount = 0;

    private long createdAt;
}
