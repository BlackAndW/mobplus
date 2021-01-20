package com.yeecloud.adplus.admin.controller.app.vo;

import com.yeecloud.adplus.admin.common.vo.BaseVO;
import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/1/18
 */
@Data
public class AppActivityVO extends BaseVO {

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
