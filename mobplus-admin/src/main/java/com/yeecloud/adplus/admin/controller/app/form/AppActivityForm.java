package com.yeecloud.adplus.admin.controller.app.form;

import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.AppVersion;
import com.yeecloud.adplus.dal.entity.Channel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/1/20
 */

@Data
public class AppActivityForm {

    private Integer appId;

    /** 应用版本 */
    @NotNull(message = "请选择应用版本")
    private List<String> appVersionCheckList;

    /** 应用渠道 */
    @NotNull(message = "请选择应用渠道")
    private List<String> channelCheckList;

    /** 活动名称 */
    @NotBlank(message = "请输入活动名称")
    private String name;

    /** 每日最多抽奖次数 */
    @NotNull(message = "请输入抽奖次数")
    private Integer drawCounts;

    /** 抽奖消耗的金币 */
    @NotNull(message = "请输入消耗的金币")
    private Integer needCoin;

    /** 抽奖最多赠送的金币 */
    @NotNull(message = "请输入奖励金币的最大值")
    private Integer maxBonusCoin;

    /** 抽奖最少赠送的金币 */
    @NotNull(message = "请输入奖励金币的最小值")
    private Integer minBonusCoin;

    /** 活动版本 */
    @NotNull(message = "请输入活动版本")
    private Integer version;

    private String remark;

    @NotNull(message = "请选择应用状态")

    private Integer status;
}
