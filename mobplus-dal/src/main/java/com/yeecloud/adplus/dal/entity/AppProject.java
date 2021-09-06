package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-02 15:53
 */
@Data
@Entity
@Table(name = "t_app_project")
public class AppProject extends AuditorEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_id")
    private App app;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_version_id")
    private AppVersion appVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_channel_id")
    private Channel channel;

    @Column(name = "c_name")
    private String name;

    @Column(name = "c_code")
    private String code;

    @Column(name = "c_remark")
    private String remark;

    @Column(name = "n_status")
    private Integer status;

}
