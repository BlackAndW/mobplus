package com.yeecloud.adplus.admin.controller.app.form;

import com.alibaba.fastjson.JSONArray;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.Channel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/1/28
 */

@Data
public class AppFunctionForm {

    @NotNull
    private Integer appId;

    @NotNull(message = "请选择功能")
    private Integer functionCode;

    /** 应用版本 */
    @NotNull(message = "请选择应用版本")
    private List<String> appVersionCheckList;

    /** 应用渠道 */
    @NotNull(message = "请选择应用渠道")
    private List<String> channelCheckList;

    /** 功能名称 */
    @NotBlank(message = "请输入功能名称")
    private String name;

    /** 广告类型列表 */
    @NotBlank(message = "请选择广告名类型")
    private String adTypeList;

    private String remark;

    private Integer status;
}
