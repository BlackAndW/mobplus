package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * @author: Leonard
 * @create: 2021/1/25
 */

@Data
public class AppActivityTaskVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /** 任务功能code */
    private Integer taskFunctionCode;

    /** 任务类别 1: 新手任务 2: 日常任务*/
    private Integer taskType;

    /** 任务名称 */
    private String taskName;

    /** 任务奖励金币 */
    private Integer taskBonusCoin;

    private Integer status;
}
