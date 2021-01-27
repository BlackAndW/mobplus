package com.yeecloud.adplus.dal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/1/18
 */
@Data
@Entity
@Table(name = "t_app_activity_task")
public class AppActivityTask extends AuditorEntity{

    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 所属活动 */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_activity_id")
    private AppActivity appActivity;

    /** 任务功能code */
    @Column(name = "task_function_code")
    private Integer taskFunctionCode;

    /** 任务类别 */
    @Column(name = "task_type")
    private Integer taskType;

    /** 任务名称 */
    @Column(name = "task_name")
    private String taskName;

    /** 任务奖励金币 */
    @Column(name = "task_bonus_coin")
    private Integer taskBonusCoin;

    @Column(name = "c_remark")
    private String remark;

    @Column(name = "n_status")
    private Integer status;
}
