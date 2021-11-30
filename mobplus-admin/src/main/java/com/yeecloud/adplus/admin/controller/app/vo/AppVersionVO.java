package com.yeecloud.adplus.admin.controller.app.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Huang
 * @create: 2020-12-02 14:52
 */
@Data
public class AppVersionVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer appId;
    private String code;
    private String content;
    private Long createdAt;
    private Long modifiedAt;

    private Integer updateMethod;
    private String gpUrl;
    private String localUrl;
}
