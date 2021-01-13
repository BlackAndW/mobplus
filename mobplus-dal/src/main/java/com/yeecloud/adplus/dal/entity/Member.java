package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Huang
 * @create: 2020-12-10 16:47
 */
@Data
@Entity
@Table(name = "t_member")
public class Member extends AuditorEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_id")
    private App app;

    @Column(name = "c_unique_id")
    private String uniqueId;

    @Column(name = "c_user_name", updatable = false)
    private String userName;

    @Column(name = "c_display_name")
    private String displayName;

    @Column(name = "c_passwd")
    private String passwd;

    @Column(name = "c_salt")
    private String salt;

    @Column(name = "c_name")
    private String name;

    @Column(name = "n_gender")
    private Integer gender;

    @Column(name = "c_avatar")
    private String avatar;

    @Column(name = "c_email")
    private String email;

    @Column(name = "c_mobile")
    private String mobile;

    @Column(name = "n_vip_level")
    private Integer vipLevel;

    @Column(name = "n_expire_at")
    private Long expireAt;

    @Column(name = "n_balance")
    private BigDecimal balance;

    @Column(name = "n_gold")
    private Integer gold;

    @Column(name = "n_invitee")
    private Integer invitee;

    @Column(name = "n_status")
    private Integer status;

    @Column(name = "n_last_login_at")
    private Long lastLoginAt;
}
