package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/6/10
 */
@Data
@Entity
@Table(name = "t_ad_account")
public class AdAccount extends AuditorEntity{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /***
     * 账号名
     */
    @Column(name = "n_name")
    private String name;

    /***
     * 账号id
     */
    @Column(name = "n_account_id")
    private String accountId;

    /**
     * 广告平台
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_adv_id")
    private Advertiser advertiser;

    /***
     * 账号所属域名
     */
    @Column(name = "n_domain")
    private String domain;
}
