package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/8/20
 */
@Data
@Entity
@Table(name = "t_image_feedback")
public class Feedback extends AuditorEntity{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 满意度1(满意)~4(不满意) */
    @Column(name = "n_evaluate")
    private Integer evaluate;

    /** 不准确的类型 */
    @Column(name = "n_err_type")
    private String errType;

    /** 意见文本 */
    @Column(name = "n_suggestion")
    private String suggestion;
}
