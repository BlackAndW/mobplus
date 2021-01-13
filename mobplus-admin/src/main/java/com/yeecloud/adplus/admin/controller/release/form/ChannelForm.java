package com.yeecloud.adplus.admin.controller.release.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: Huang
 * @create: 2020-12-01 14:29
 */
@Data
public class ChannelForm {

    @NotBlank(message = "请输入渠道名称")
    private String name;
    @NotBlank(message = "请输入渠道编码")
    private String code;
    @NotNull(message = "请选择渠道类型")
    private Integer type;
    private Integer sort;
    @NotNull(message = "请选择渠道状态")
    private Integer status;
    private String remark;
}
