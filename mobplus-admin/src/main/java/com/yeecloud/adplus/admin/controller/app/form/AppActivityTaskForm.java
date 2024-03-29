package com.yeecloud.adplus.admin.controller.app.form;

import com.yeecloud.adplus.dal.entity.AppActivity;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: Leonard
 * @create: 2021/1/21
 */

@Data
public class AppActivityTaskForm {

    private AppActivity appActivity;

    /** 任务功能code */
    @NotNull(message = "请输入任务功能code")
    private Integer taskFunctionCode;

    /** 任务类别 */
    @NotNull(message = "请选择任务类别")
    private Integer taskType;

    /** 任务名称 */
    @NotBlank(message = "请输入任务名称")
    private String taskName;

    /** 任务奖励金币 */
    @NotNull(message = "请输入任务奖励金币")
    private Integer taskBonusCoin;

    private String remark;

    private Integer status;
}
