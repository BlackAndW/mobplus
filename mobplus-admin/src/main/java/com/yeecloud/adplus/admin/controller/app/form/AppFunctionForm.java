package com.yeecloud.adplus.admin.controller.app.form;

import com.alibaba.fastjson.JSONArray;
import com.yeecloud.adplus.dal.entity.App;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: Leonard
 * @create: 2021/1/28
 */

@Data
public class AppFunctionForm {

    private App app;

    /** 功能名称 */
    @NotBlank(message = "请输入功能名称")
    private String name;

    /** 广告类型列表 */
    @NotBlank(message = "请选择广告名类型")
    private String adTypeList;

    private String remark;

    private Integer status;
}
