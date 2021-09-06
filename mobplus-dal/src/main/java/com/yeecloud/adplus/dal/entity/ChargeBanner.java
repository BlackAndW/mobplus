package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
@Entity
@Table(name = "t_charge_banner")
public class ChargeBanner extends AuditorEntity{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "n_name")
    private String name;

    /** banner源地址 */
    @Column(name = "n_img_path")
    private String imgPath;

    /** banner类型：1：应用内跳转，2，浏览器外跳转 */
    @Column(name = "n_img_type")
    private Integer imgType;

    /** banner点击后跳转的url */
    @Column(name = "n_img_url")
    private String imgUrl;

    @Column(name = "n_order")
    private Integer order;
}
