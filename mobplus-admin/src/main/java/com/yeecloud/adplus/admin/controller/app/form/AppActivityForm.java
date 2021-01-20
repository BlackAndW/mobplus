package com.yeecloud.adplus.admin.controller.app.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: Leonard
 * @create: 2021/1/20
 */

@Data
public class AppActivityForm {

    private Integer id;

    /** 活动名称 */
    @NotBlank(message = "请输入活动名称")
    private String name;

    /** 每日最多抽奖次数 */
    @NotBlank(message = "请输入抽奖次数")
    private int drawCounts;

    /** 抽奖消耗的金币 */
    @NotBlank(message = "请输入消耗的金币")
    private int needCoin;

    /** 抽奖最多赠送的金币 */
    @NotBlank(message = "请输入奖励金币的最大值")
    private int maxBonusCoin;

    /** 抽奖最少赠送的金币 */
    @NotBlank(message = "请输入奖励金币的最小值")
    private int minBonusCoin;

    /** 活动版本 */
    @NotBlank(message = "请输入活动版本")
    private int version;

    private String remark;

    @NotBlank(message = "请选择应用状态")
    private Integer status;
}
