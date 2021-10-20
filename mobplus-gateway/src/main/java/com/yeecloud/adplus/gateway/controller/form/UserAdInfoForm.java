package com.yeecloud.adplus.gateway.controller.form;

import io.github.yedaxia.apidocs.Ignore;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: Leonard
 * @create: 2021/3/23
 */

@Data
public class UserAdInfoForm {

    /** appId */
    @NotNull
    private String appId;

    /** uuid */
    @NotNull
    private String uuid;

    /** 是否从零开始统计 */
    private boolean clear;

    /** 展示位code */
    private String appPositionCode;

    /** 渠道 */
    private String adChannel;

    /** 广告请求次数 传总次数，后台不累加*/
    private Integer adRequestCount;

    /** 广告展示次数 传总次数，后台不累加*/
    private Integer adShowCount;

    /** 广告点击次数 传总次数，后台不累加*/
    private Integer adClickCount;

}
