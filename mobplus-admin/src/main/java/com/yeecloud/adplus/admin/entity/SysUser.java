package com.yeecloud.adplus.admin.entity;

import com.yeecloud.adplus.dal.entity.AuditorEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 系统用户
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Data
@Entity
@Table(name = "sys_user")
public class SysUser extends AuditorEntity {

    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id")
    private Integer id;

    /** 用户名 */
    @Column(name = "c_user_name", updatable = false)
    private String userName;

    /** 显示名称 */
    @Column(name = "c_display_name")
    private String displayName;

    /** 密码 */
    @Column(name = "c_passwd")
    private String passwd;

    /** 加密Salt */
    @Column(name = "c_salt")
    private String salt;

    /** 真实名 */
    @Column(name = "c_name")
    private String name;

    /** 性别 0:女 1:男 2:未知 */
    @Column(name = "n_gender")
    private Integer gender;

    /** 头像 */
    @Column(name = "c_avatar")
    private String avatar;

    /** 邮箱 */
    @Column(name = "c_email")
    private String email;

    /** 手机号 */
    @Column(name = "c_mobile")
    private String mobile;

    /** 生日 */
    @Column(name = "d_birthday")
    private Date birthday;
    
    /** 状态 */
    @Column(name = "n_status")
    private Integer status;

    /** 备注 */
    @Column(name = "c_remark")
    private String remark;

    /**
     * 用户角色列表
     */
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "sys_user_role", joinColumns = @JoinColumn(name = "n_user_id"), inverseJoinColumns = @JoinColumn(name = "n_role_id"))
    private List<SysRole> roles;
}
