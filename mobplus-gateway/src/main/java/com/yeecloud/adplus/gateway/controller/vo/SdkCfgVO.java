package com.yeecloud.adplus.gateway.controller.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Maps;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: Huang
 * @create: 2020-12-09 14:22
 */
@Data
public class SdkCfgVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean enabled = true;
    private long interval = 1000 * 60 * 5;//5分钟
    private String notifyUrl;
    private String umengAppKey;

    @JSONField(name = "adv")
    private Map<String, AdvertiserCfgVO> advertiserList = Maps.newHashMap();
    @JSONField(name = "pos")
    private Map<String, AppPosCfgVO> adPosList = Maps.newHashMap();
}
