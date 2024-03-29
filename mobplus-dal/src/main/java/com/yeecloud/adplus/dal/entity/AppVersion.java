package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Huang
 * @create: 2020-12-02 14:32
 */
@Data
@Entity
@Table(name = "t_app_version")
public class AppVersion extends AuditorEntity {
    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_id")
    private App app;

    @Column(name = "c_code")
    private String code;

    @Column(name = "c_content")
    private String content;

    @Column(name = "n_status")
    private Integer status;

}

