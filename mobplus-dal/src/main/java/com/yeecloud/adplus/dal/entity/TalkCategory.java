package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "t_talk_category")
public class TalkCategory extends AuditorEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "n_parent_id")
    private Integer parentId;

    @Column(name = "c_name")
    private String name;

    @Column(name = "c_icon")
    private String icon;

    @Column(name = "n_sort")
    private Integer sort;

    @Column(name = "n_level")
    private Integer level;

    @Column(name = "n_status")
    private Integer status;

    @OneToMany
    @JoinColumn(name = "n_parent_id")
    @OrderBy("n_sort asc ")
    private List<TalkCategory> children;
}
