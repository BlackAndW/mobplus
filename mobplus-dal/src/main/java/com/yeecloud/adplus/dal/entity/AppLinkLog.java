package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/9/1
 */
@Data
@Entity
@Table(name = "t_app_link_log")
public class AppLinkLog extends AuditorEntity{

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_link_id")
    private AppLink appLink;

    /** 点击次数 */
    @Column(name = "n_click_num")
    private Integer clickNum = 0;

    /** 展示次数 */
    @Column(name = "n_show_num")
    private Integer showNum = 0;
}
