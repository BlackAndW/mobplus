package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Leonard
 * @create: 2021/1/20
 */

@Data
public class AppActivityVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 活动名称 */
    private String name;

    /** 每日最多抽奖次数 */
    private Integer drawCounts;

    /** 抽奖消耗的金币 */
    private Integer needCoin;

    /** 抽奖最多赠送的金币 */
    private Integer maxBonusCoin;

    /** 抽奖最少赠送的金币 */
    private Integer minBonusCoin;

    private String remark;

    private Integer status;
}
