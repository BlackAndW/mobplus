package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/9/2
 */
@Data
@Entity
@Table(name = "t_user_feedback")
public class UserFeedback extends AuditorEntity{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_id")
    private App app;

    @Column(name = "n_username")
    private String username;

    @Column(name = "n_pkg_version")
    private String version;

    @Column(name = "n_device")
    private String device;

    @Column(name = "n_os")
    private String os;

    @Column(name = "n_ip")
    private String ip;

    @Column(name = "n_area")
    private String area;

    @Column(name = "n_phone")
    private String phone;

    /** 留言评价内容 */
    @Column(name = "n_content")
    private String content;

    /** 用户联系邮箱 */
    @Column(name = "n_email")
    private String email;

    /** 处理状态：1：未处理，2：已处理 */
    @Column(name = "n_status")
    private Integer status = 1;

    @Column(name = "n_result")
    private String result;

    @Column(name = "n_remark")
    private String remark;

}
