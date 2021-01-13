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

    /** 优先级 最小为1, 否则取不到 */
    @Column(name = "n_ratio")
    private Integer ratio = 1;

}
