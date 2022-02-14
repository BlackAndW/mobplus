package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @date: 2022/2/11 / 17:32
 */
@Data
@Entity
@Table(name = "t_ad_test")
public class AdTest extends AuditorEntity{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_adv_id")
    private Advertiser advertiser;

    /** 广告类型 */
    @Column(name = "n_ad_type")
    private Integer adType;

    /** 测试广告id */
    @Column(name = "n_test_id")
    private String testId;
}
