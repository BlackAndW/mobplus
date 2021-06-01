package com.yeecloud.adplus.gateway.controller.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Huang
 * @create: 2020-12-09 14:47
 */
@Data
public class AdPositionVO implements Serializable {
    @JSONField(name = "adv")
    private String advertiser;
    private String posId;
    private String unitId;
    private int ratio;
    private int adType;
    private int typeRatio;
    private int limitShowCount;
    private int limitClickCount;
}
