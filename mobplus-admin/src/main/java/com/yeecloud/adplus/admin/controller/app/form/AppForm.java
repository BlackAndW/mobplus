package com.yeecloud.adplus.admin.controller.app.form;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: Huang
 * @create: 2020-12-02 10:11
 */
@Data
public class AppForm {

    @NotBlank(message = "请输入应用名称")
    private String name;
    @NotBlank(message = "请输入应用包名")
    private String pkgName;
    @NotNull(message = "请选择运行环境")
    private Integer runtime;
    @NotNull(message = "请选择应用类型")
    private Integer type;
    private String iconPath;
    private String remark;
    @NotNull(message = "请选择应用状态")
    private Integer status;
    private JSONObject extra;

}
