package com.yeecloud.adplus.gateway.controller.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Huang
 * @create: 2020-11-30 15:17
 */
@Data
public class AppConfigVO implements Serializable {
    @JSONField(name = "ad")
    private Integer adSwitch = 1;

    @JSONField(name = "cms")
    private Integer contentSwitch = 1;

    @JSONField(name = "index")
    private Integer indexSwitch = 1;

    private Object conf;

}
