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

    private static final long serialVersionUID = 1L;

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

    @Column(name = "n_update_method")
    private Integer updateMethod;

    @Column(name = "n_gp_url")
    private String gpUrl;

    @Column(name = "n_local_url")
    private String localUrl;

}

