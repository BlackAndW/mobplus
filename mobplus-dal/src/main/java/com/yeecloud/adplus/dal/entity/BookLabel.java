package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2022/4/2
 */
@Data
@Entity
@Table(name = "t_book_label")
public class BookLabel extends AuditorEntity{

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "n_name")
    private String name;

    @Column(name = "n_zh_name")
    private String zhName;

    @Column(name = "n_label_type")
    private String labelType;

}
