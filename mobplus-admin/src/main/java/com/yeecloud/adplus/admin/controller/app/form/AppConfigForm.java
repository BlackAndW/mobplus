package com.yeecloud.adplus.admin.controller.app.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: Huang
 * @create: 2020-12-02 17:34
 */
@Data
public class AppConfigForm {

    @NotNull(message = "请选择广告开关")
    private Integer adSwitch;
    @NotNull(message = "请选择内容开关")
    private Integer contentSwitch;
    @NotNull(message = "请选择首页开关")
    private Integer indexSwitch;
    private String conf;
    @NotNull(message = "请选择配置状态")
    private Integer status;
}
