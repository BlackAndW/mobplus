package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/8/31
 */
@Data
@Entity
@Table(name = "t_game_log")
public class GameLog extends AuditorEntity{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_game_id")
    private Game game;

    @Column(name = "n_click_num")
    private Integer clickNum = 0;

    @Column(name = "n_ip_num")
    private Integer ipNum = 0;

    @Column(name = "n_play_time")
    private Integer playTime = 0;
}
