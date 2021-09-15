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

    /** 广告开关 JSON处理后返回字段名: ad */
    @JSONField(name = "ad")
    private Integer adSwitch = 1;

    /** 内容开关 JSON处理后返回字段名: content */
    @JSONField(name = "content")
    private Integer contentSwitch = 1;

    /** 首页开关 JSON处理后返回字段名: index */
    @JSONField(name = "index")
    private Integer indexSwitch = 1;

    /** 配置 */
    private Object conf;

}
