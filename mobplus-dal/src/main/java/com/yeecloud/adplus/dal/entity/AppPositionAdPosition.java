package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Huang
 * @create: 2020-12-08 17:25
 */
@Data
@Entity
@Table(name = "t_app_pos_ad_pos")
public class AppPositionAdPosition extends AuditorEntity {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 展示位 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_pos_id")
    private AppPosition appPosition;

    /** 广告位 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_ad_pos_id")
    private AdPosition adPosition;

    /** 平台权重 优先级 最小为1, 否则取不到 */
    @Column(name = "n_ratio")
    private Integer ratio = 1;

    /** 类型权重 优先级 最小为1, 否则取不到 */
    @Column(name = "n_type_ratio")
    private Integer typeRatio = 1;

    /**
     * 广告限制展示次数
     */
    @Column(name = "n_limit_show_count")
    private Integer limitShowCount = 3;

    /**
     * 广告限制点击次数
     */
    @Column(name = "n_limit_click_count")
    private Integer limitClickCount = 2;
}
