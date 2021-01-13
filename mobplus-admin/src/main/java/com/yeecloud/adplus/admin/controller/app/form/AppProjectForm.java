package com.yeecloud.adplus.admin.controller.app.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: Huang
 * @create: 2020-12-02 16:04
 */
@Data
public class AppProjectForm {
    @NotNull
    private Integer appId;
    @NotNull(message = "请选择应用版本")
    private Integer appVersionId;
    @NotNull(message = "请选择渠道")
    private Integer channelId;
    @NotNull(message = "请选择项目状态")
    private Integer status;
    private String remark;
}
