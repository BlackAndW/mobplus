package com.yeecloud.adplus.admin.controller.cms.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
public class ChargeMTypeForm {

    @NotNull(message = "appId不能为空")
    private Integer appId;

    private String name;

    private String enName;

    private Integer style;

    private Integer isAll;

    private String orderColumn;

    private Integer order;

    private Integer rankOrder;
}
