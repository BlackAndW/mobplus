package com.yeecloud.adplus.gateway.controller.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: Leonard
 * @create: 2021/9/3
 */
@Data
public class UserFeedbackForm {

    /** ---appId */
    @NotNull
    private String appId;

    /** ---版本号 ---必传 */
    @NotNull
    private String version;

    /** ---设备名 */
    private String device;

    /** ---操作系统 */
    private String os;

    /** ---评价内容 */
    private String content;

    /** ---email */
    private String email;

    /** ---反馈类型 ---1:应用崩溃，2:无响应，3:功能异常，4:消耗流量，5:投诉，6:广告，7:通知，8:建议 */
    private Integer type;
}
