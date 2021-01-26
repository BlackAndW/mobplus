package com.yeecloud.adplus.admin.controller.app.vo;

import com.yeecloud.adplus.admin.common.vo.BaseVO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Leonard
 * @create: 2021/1/21
 */

@Data
public class AppActivityTaskVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer activityId;

    /** 任务功能code */
    private Integer taskFunctionCode;

    /** 任务功能名 */
    private String taskFunctionName;

    /** 任务类别 */
    private int taskType;

    /** 任务名称 */
    private String taskName;

    /** 任务奖励金币 */
    private int taskBonusCoin;

    private String remark;

    private Integer status;
}
