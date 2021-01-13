package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Huang
 * @create: 2020-12-09 10:39
 */
@Data
@Entity
@Table(name = "t_app_advertiser")
public class AppAdvertiser extends AuditorEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 应用
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_id")
    private App app;

    /**
     * 广告平台
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_adv_id")
    private Advertiser advertiser;

    /**
     * 广告平台的appId
     */
    @Column(name = "c_app_id")
    private String appId;

    /**
     * 广告平台的appKey
     */
    @Column(name = "c_app_key")
    private String appKey;

    /**
     * 广告平台的应用名
     */
    @Column(name = "c_app_name")
    private String appName;

}
