package com.yeecloud.adplus.admin.entity;

import com.yeecloud.adplus.dal.entity.AuditorEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 系统配置
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Data
@Entity
@Table(name = "sys_conf")
public class SysConf extends AuditorEntity {

    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id")
    private Integer id;

    /** 配置分类 */
    @Column(name = "n_category")
    private Integer category;

    /** 配置名称 */
    @Column(name = "c_name")
    private String name;

    /** 配置键 */
    @Column(name = "c_key")
    private String key;

    /** 配置值 */
    @Column(name = "c_value")
    private String value;

    /** 权限 0:UI不可见,不可操作 1:UI可见,不可操作 2:UI可见,可操作 */
    @Column(name = "n_perm")
    private int perm = 2;

    /** 状态 */
    @Column(name = "n_status")
    private int status;

    /** 排序 */
    @Column(name = "n_sort")
    private int sort;

    /** 备注 */
    @Column(name = "c_remark")
    private String remark;
}
