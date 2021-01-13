package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "t_talk")
public class Talk extends AuditorEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_category_id")
    private TalkCategory category;

    @Column(name = "c_title")
    private String title;

    @OneToMany
    @JoinColumn(name = "n_talk_id")
    @OrderBy("n_sort asc")
    private List<TalkContent> content;
}
