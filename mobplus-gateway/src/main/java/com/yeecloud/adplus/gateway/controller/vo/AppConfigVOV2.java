package com.yeecloud.adplus.gateway.controller.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Huang
 * @create: 2020-12-15 15:35
 */
@Data
public class AppConfigVOV2 implements Serializable {
    @JSONField(name = "ad")
    private Integer adSwitch = 1;

    @JSONField(name = "content")
    private Integer contentSwitch = 1;

    @JSONField(name = "index")
    private Integer indexSwitch = 1;

    private Object conf;

}
