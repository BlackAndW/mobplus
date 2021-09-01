package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
@Entity
@Table(name = "t_app_link")
public class AppLink extends AuditorEntity{

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_id")
    private App app;

    @Column(name = "n_name")
    private String name;

    /** 图片源地址 */
    @Column(name = "n_img_path")
    private String imgPath;

    /** 类型 */
    @Column(name = "n_img_type")
    private Integer imgType;

    /** 点击后跳转的url */
    @Column(name = "n_img_url")
    private String imgUrl;

//    /** 点击次数 */
//    @Column(name = "n_click_num")
//    private Integer clickNum = 0;
//
//    /** 展示次数 */
//    @Column(name = "n_show_num")
//    private Integer showNum = 0;

    /** 游戏介绍 */
    @Column(name = "n_description")
    private String description;

}
