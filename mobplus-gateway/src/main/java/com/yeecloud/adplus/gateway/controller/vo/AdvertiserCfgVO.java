package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Huang
 * @create: 2020-12-09 14:24
 */
@Data
public class AdvertiserCfgVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String appId;
    private String appKey;
    private String appName;
}
