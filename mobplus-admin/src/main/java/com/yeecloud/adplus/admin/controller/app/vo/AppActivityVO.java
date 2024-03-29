package com.yeecloud.adplus.admin.controller.app.vo;

import com.yeecloud.adplus.admin.common.vo.BaseVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/1/18
 */
@Data
public class AppActivityVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /** 应用版本 */
    private List<String> appVersionCheckList;

    /** 应用渠道 */
    private List<String> channelCheckList;

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
