package com.yeecloud.adplus.dal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/1/18
 */
@Data
@Entity
@Table(name = "t_app_activity")
public class AppActivity extends AuditorEntity{

    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 应用版本 */
    @OneToOne
    @JoinColumn(name = "n_app_version_id")
    private AppVersion appVersion;

    /** 应用渠道 */
    @OneToOne
    @JoinColumn(name = "n_channel_id")
    private Channel channel;

    /** 活动名称 */
    @Column(name = "c_name")
    private String name;

    /** 所属应用 */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_id")
    private App app;

    /** 每日最多抽奖次数 */
    @Column(name = "draw_counts")
    private Integer drawCounts;

    /** 抽奖消耗的金币 */
    @Column(name = "need_coin")
    private Integer needCoin;

    /** 抽奖最多赠送的金币 */
    @Column(name = "max_bonus_coin")
    private Integer maxBonusCoin;

    /** 抽奖最少赠送的金币 */
    @Column(name = "min_bonus_coin")
    private Integer minBonusCoin;

    @Column(name = "c_remark")
    private String remark;

    @Column(name = "n_status")
    private Integer status;
}
