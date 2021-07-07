package com.yeecloud.adplus.dal.entity;

import lombok.Data;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
@Entity
@Table(name = "t_charge_m_type")
public class ChargeMType extends AuditorEntity{

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "n_name")
    private String name;

    /** 类型内视频排序依据列 */
    @Column(name = "c_order_column")
    private String orderColumn;

    /** 类型内视频升降序规则：1为降序，2为升序 */
    @Column(name = "c_order_rule")
    private Integer order;

    /** 类型优先级：1为最高 */
    @Column(name = "n_rank_order")
    private Integer rankOrder;
}
