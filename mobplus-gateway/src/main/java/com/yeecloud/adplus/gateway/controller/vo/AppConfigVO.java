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

    /** 广告开关 JSON处理后返回字段名: ad */
    @JSONField(name = "ad")
    private Integer adSwitch = 1;

    /** 内容开关 JSON处理后返回字段名: cms */
    @JSONField(name = "cms")
    private Integer contentSwitch = 1;

    /** 首页开关 JSON处理后返回字段名: index */
    @JSONField(name = "index")
    private Integer indexSwitch = 1;

    /** 配置 */
    private Object conf;

}
