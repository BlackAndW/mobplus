package com.yeecloud.adplus.admin.controller.app.form;

import com.yeecloud.adplus.dal.entity.App;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: Huang
 * @create: 2020-12-15 09:52
 */
@Data
public class AppMobileConfForm {

    private App app;
    @NotBlank(message = "请输入配置名称")
    private String name;
    @NotBlank(message = "请输入配置键")
    private String key;
    @NotBlank(message = "请输入配置值")
    private String value;
    private String remark;
    @NotNull(message = "请选择状态")
    private Integer status;
}
