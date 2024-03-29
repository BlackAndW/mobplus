package com.yeecloud.adplus.admin.controller.app.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Huang
 * @create: 2020-12-08 17:53
 */
@Data
public class AppPositionVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer appId;
    private String appName;
    private String code;
    private String name;
    private Integer status;

    private Integer createdBy;
    private Integer modifiedBy;
    private Long createdAt;
    private Long modifiedAt;
}
