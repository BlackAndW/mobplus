package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Huang
 * @create: 2020-12-14 18:13
 */
@Data
@Entity
@Table(name = "t_app_mobile_conf")
public class AppMobileConf extends AuditorEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 所属应用
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_id")
    private App app;

    /** 应用版本 */
    @Column(name = "n_app_version_list")
    private String appVersionList;

    /** 应用渠道 */
    @Column(name = "n_channel_list")
    private String channelList;

    @Column(name = "c_name")
    private String name;

    @Column(name = "c_key")
    private String key;

    @Column(name = "c_value")
    private String value;

    @Column(name = "c_remark")
    private String remark;

    @Column(name = "n_status")
    private Integer status;

}
