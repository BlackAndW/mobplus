package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Huang
 * @create: 2020-12-08 15:31
 */
@Data
@Entity
@Table(name = "t_advertiser")
public class Advertiser extends AuditorEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 平台代码 */
    @Column(name = "c_code")
    private String code;

    /** 平台名称 */
    @Column(name = "c_name")
    private String name;

    /** 备注 */
    @Column(name = "c_remark")
    private String remark;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_ad_test_id")
    private Integer adTestId;
}
