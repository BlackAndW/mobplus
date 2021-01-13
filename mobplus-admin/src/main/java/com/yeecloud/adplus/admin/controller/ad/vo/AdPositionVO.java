package com.yeecloud.adplus.admin.controller.ad.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Huang
 * @create: 2020-12-08 16:28
 */
@Data
public class AdPositionVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer appId;
    private String appName;
    private Integer advId;
    private String advName;
    private String code;
    private String name;
    private Integer type;
    private String typeName;
    private String remark;

    private Integer createdBy;
    private Integer modifiedBy;
    private Long createdAt;
    private Long modifiedAt;
}
