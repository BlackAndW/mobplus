package com.yeecloud.adplus.admin.controller.app.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Huang
 * @create: 2020-12-15 09:46
 */
@Data
public class AppMobileConfVO implements Serializable {

    private Integer id;
    private Integer appId;
    private String name;
    private String key;
    private String value;
    private String remark;
    private Integer status;
    private Long createdAt;
    private Long modifiedAt;

}
