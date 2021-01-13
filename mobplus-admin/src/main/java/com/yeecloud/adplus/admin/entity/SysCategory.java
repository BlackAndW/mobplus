package com.yeecloud.adplus.admin.entity;

import com.yeecloud.adplus.dal.entity.AuditorEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 数据分类
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Data
@Entity
@Table(name = "sys_category")
public class SysCategory extends AuditorEntity {

    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id")
    private Integer id;

    /** 上级分类 */
    @Column(name = "n_parent_id")
    private Integer parentId;

    /** 分类名称 */
    @Column(name = "c_name")
    private String name;

    /** 英文名称 */
    @Column(name = "c_name_en")
    private String nameEn;

    /** 状态 */
    @Column(name = "n_status")
    private Integer status;

    /** 排序 */
    @Column(name = "n_sort")
    private Integer sort;

    /** 备注 */
    @Column(name = "c_remark")
    private String remark;

    /**
     * 下级分类
     */
    @OneToMany
    @JoinColumn(name = "n_parent_id")
    @OrderBy("n_sort asc")
    private List<SysCategory> children;
}
