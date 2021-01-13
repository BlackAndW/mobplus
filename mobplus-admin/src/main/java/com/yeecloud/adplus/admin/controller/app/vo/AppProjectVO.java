package com.yeecloud.adplus.admin.controller.app.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Huang
 * @create: 2020-12-02 16:02
 */
@Data
public class AppProjectVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer appId;
    private Integer appVersionId;
    private String appVersionCode;
    private Integer channelId;
    private String channelName;
    private String name;
    private String code;
    private Integer status;
    private String remark;
    private Long createdAt;
    private Long modifiedAt;

}
