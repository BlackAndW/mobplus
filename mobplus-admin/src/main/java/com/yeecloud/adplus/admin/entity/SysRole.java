package com.yeecloud.adplus.admin.entity;

import com.yeecloud.adplus.dal.entity.AuditorEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 系统角色
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Data
@Entity
@Table(name = "sys_role")
public class SysRole extends AuditorEntity {

    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id")
    private Integer id;

    /** 角色编码 */
    @Column(name = "c_code")
    private String code;

    /** 角色名称 */
    @Column(name = "c_name")
    private String name;

    /** 排序 */
    @Column(name = "n_sort")
    private Integer sort;

    /** 状态 */
    @Column(name = "n_status")
    private Integer status;

    /** 备注 */
    @Column(name = "c_remark")
    private String remark;

    /**
     * 角色用户列表
     */
    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "roles", fetch = FetchType.LAZY)
    private List<SysUser> users;

    /**
     * 角色权限列表
     */
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "sys_role_permission", joinColumns = @JoinColumn(name = "n_role_id"), inverseJoinColumns = @JoinColumn(name = "n_permission_id"))
    private List<SysPermission> permissions;
}
