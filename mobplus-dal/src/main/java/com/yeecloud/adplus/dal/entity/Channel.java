package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 发行渠道
 *
 * @author: Huang
 * @create: 2020-12-01 10:37
 */
@Data
@Entity
@Table(name = "t_channel")
public class Channel extends AuditorEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 渠道名称
     **/
    @Column(name = "c_name")
    private String name;

    /**
     * 渠道编码
     **/
    @Column(name = "c_code")
    private String code;

    /**
     * 渠道类型（1：应用市场，2：厂商渠道）
     **/
    @Column(name = "n_type")
    private Integer type;

    /**
     * 排序
     **/
    @Column(name = "n_sort")
    private Integer sort;

    /**
     * 渠道状态(1：未启用，2：已启用)
     **/
    @Column(name = "n_status")
    private Integer status;

    /**
     * 备注
     */
    @Column(name = "c_remark")
    private String remark;

}
