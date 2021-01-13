package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Huang
 * @create: 2020-12-08 16:03
 */
@Data
@Entity
@Table(name = "t_ad_position")
public class AdPosition extends AuditorEntity {
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
     * 在广告平台的广告位代码
     */
    @Column(name = "c_code")
    private String code;

    /**
     * 广告平台的广告位名称
     */
    @Column(name = "c_name")
    private String name;

    /**
     * 广告位类型
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_type")
    private AdType type;

    /**
     * 备注
     */
    @Column(name = "c_remark")
    private String remark;
}
