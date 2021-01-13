package com.yeecloud.adplus.admin.entity;

import com.yeecloud.adplus.dal.entity.AuditorEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 权限信息
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Data
@Entity
@Table(name = "sys_permission")
public class SysPermission extends AuditorEntity {

    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id")
    private Integer id;

    /** 分类 */
    @Column(name = "n_category")
    private Integer category;

    /** 代码 */
    @Column(name = "c_code")
    private String code;

    /** 权限名称 */
    @Column(name = "c_name")
    private String name;

    /** 权限英文名称 */
    @Column(name = "c_name_en")
    private String nameEn;

    /** 动作  UI上用,   CURDEI */
    @Column(name = "c_action")
    private String action;

    /** 排序 */
    @Column(name = "n_sort")
    private Integer sort;

    /** 状态 0, 1,2,3,4,5 */
    @Column(name = "n_status")
    private Integer status;

    /** 备注 */
    @Column(name = "c_remark")
    private String remark;

    /**
     * 具有该权限的角色列表
     */
    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "permissions", fetch = FetchType.LAZY)
    private List<SysRole> roles;


    /**
     * 具有该权限的角色列表
     */
    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "permissions", fetch = FetchType.LAZY)
    private List<SysMenu> menus;
}
