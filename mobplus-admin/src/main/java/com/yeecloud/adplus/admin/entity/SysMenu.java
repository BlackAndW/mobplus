package com.yeecloud.adplus.admin.entity;

import com.yeecloud.adplus.dal.entity.AuditorEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 菜单信息
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Data
@Entity
@Table(name = "sys_menu")
public class SysMenu extends AuditorEntity {

    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id")
    private Integer id;

    /** 上级菜单 */
    @Column(name = "n_parent_id")
    private Integer parentId;

    /** 菜单标题 */
    @Column(name = "c_title")
    private String title;

    /** 英文标题 */
    @Column(name = "c_title_en")
    private String titleEn;

    /** 菜单名称 */
    @Column(name = "c_name")
    private String name;

    /** 图标 */
    @Column(name = "c_icon")
    private String icon;

    /** 路径 */
    @Column(name = "c_path")
    private String path;

    /** 组件 */
    @Column(name = "c_component")
    private String component;

    /** 跳转地址 (一级菜单) */
    @Column(name = "c_redirect")
    private String redirect;

    /** 是否缓存该页面: 1:是 0:不是 */
    @Column(name = "n_keep_alive")
    private Boolean keepAlive;

    /** 是否隐藏路由: 0否,1是 */
    @Column(name = "n_hidden")
    private Boolean hidden;

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
     * 下级菜单
     */
    @OneToMany
    @JoinColumn(name = "n_parent_id")
    @OrderBy("n_sort asc")
    private List<SysMenu> children;

    /**
     * 菜单绑定的权限列表
     */
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "sys_menu_permission", joinColumns = @JoinColumn(name = "n_menu_id"), inverseJoinColumns = @JoinColumn(name = "n_permission_id"))
    private List<SysPermission> permissions;
}
