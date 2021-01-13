package com.yeecloud.adplus.admin.controller.release.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Huang
 * @create: 2020-12-01 15:05
 */
@Data
public class ChannelVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String code;
    private Integer type;
    private Integer sort;
    private Integer status;
    private String remark;
    private Integer createdBy;
    private Integer modifiedBy;
    private Long createdAt;
    private Long modifiedAt;
}
