package com.yeecloud.adplus.gateway.controller.form;

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

    private boolean clear;

    private String appPositionCode;

    private String adChannel;

    private Integer adRequestCount;

    private Integer adShowCount;

    private Integer adClickCount;

}
