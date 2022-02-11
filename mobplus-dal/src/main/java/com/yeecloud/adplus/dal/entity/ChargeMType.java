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

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_id")
    private App app;

    @Column(name = "n_name")
    private String name;

    @Column(name = "n_en_name")
    private String enName;

    /** 1:视频；2:壁纸 */
    @Column(name = "n_style")
    private Integer style;

    /** 0:否；1:是 */
    @Column(name = "n_isall")
    private Integer isAll;

    /** 类型内排序依据列 */
    @Column(name = "c_order_column")
    private String orderColumn;

    /** 类型内升降序规则：1为降序，2为升序 */
    @Column(name = "c_order_rule")
    private Integer order;

    /** 类型优先级：1为最高 */
    @Column(name = "n_rank_order")
    private Integer rankOrder;
}
