package com.yeecloud.adplus.admin.controller.app.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: Huang
 * @create: 2020-12-02 14:44
 */
@Data
public class AppVersionForm {

    @NotNull
    private Integer appId;
    @NotBlank(message = "请输入版本号")
    private String code;
    private String content;
    @NotNull(message = "请选择版本状态")
    private Integer status;
}
