package com.yeecloud.adplus.admin.controller.app.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Huang
 * @create: 2020-12-02 11:04
 */
@Data
public class AppVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String pkgName;
    private String appId;
    private Integer runtime;
    private Integer type;
    private Integer vpn;
    private String iconPath;
    private String remark;
    private Integer status;
    private JSONObject extra;

    private Long createdAt;
    private Long modifiedAt;
}
