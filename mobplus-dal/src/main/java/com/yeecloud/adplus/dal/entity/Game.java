package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 游戏
 *
 * Date: 2020-06-12 14:23:37
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
@Entity
@Table(name = "t_game")
public class Game extends AuditorEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 名称 */
    @Column(name = "c_name")
    private String name;

    /** 描述 */
    @Column(name = "c_desc")
    private String desc;

    /** 缩略图 */
    @Column(name = "c_thumb_url")
    private String thumbUrl;

    /** 播放地址 */
    @Column(name = "c_play_url")
    private String playUrl;

    /** 推荐类型 */
    @Column(name = "n_type")
    private String type;

    /** 状态 */
    @Column(name = "n_status")
    private int status;

    /** 备注 */
    @Column(name = "c_remark")
    private String remark;

    /** 点击次数 */
    @Column(name = "n_click_num")
    private Integer clickNum = 0;

    /** 展示次数 */
    @Column(name = "n_show_num")
    private Integer showNum = 0;
}
