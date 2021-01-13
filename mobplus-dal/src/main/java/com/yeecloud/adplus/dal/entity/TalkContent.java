package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_talk_content")
public class TalkContent extends AuditorEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_talk_id")
    private Talk talk;

    @Column(name = "n_key")
    private Integer key;

    @Column(name = "c_value")
    private String value;

    @Column(name = "n_sort")
    private Integer sort;
}
