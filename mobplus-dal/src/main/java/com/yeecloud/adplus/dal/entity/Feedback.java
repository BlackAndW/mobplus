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

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "n_evaluate")
    private Integer evaluate;

    @Column(name = "n_err_type")
    private String errType;

    @Column(name = "n_suggestion")
    private String suggestion;
}
