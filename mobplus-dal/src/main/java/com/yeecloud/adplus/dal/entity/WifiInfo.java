package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/6/23
 */

@Data
@Entity
@Table(name = "t_wifi_info")
public class WifiInfo extends AuditorEntity{

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "n_name")
    private String name;

    @Column(name = "n_gps")
    private String gps;

    @Column(name = "n_mac")
    private String mac;

    @Column(name = "n_type")
    private String type;

    @Column(name = "c_password")
    private String password;
}
