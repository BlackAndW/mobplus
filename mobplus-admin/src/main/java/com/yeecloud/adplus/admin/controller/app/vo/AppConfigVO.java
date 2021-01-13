package com.yeecloud.adplus.admin.controller.app.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Huang
 * @create: 2020-12-02 17:39
 */
@Data
public class AppConfigVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer appId;
    private Integer appProjectId;
    private Integer appVersionId;
    private Integer channelId;
    private String name;
    private Integer adSwitch;
    private Integer contentSwitch;
    private Integer indexSwitch;
    private String conf;
    private Integer status;
}
