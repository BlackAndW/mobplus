package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Huang
 * @create: 2020-12-08 15:31
 */
@Data
@Entity
@Table(name = "t_ad_type")
public class AdType extends AuditorEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 平台名称 */
    @Column(name = "c_name")
    private String name;

    /** 备注 */
    @Column(name = "c_remark")
    private String remark;
}
