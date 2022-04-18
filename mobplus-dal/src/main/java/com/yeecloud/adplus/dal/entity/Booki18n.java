package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2022/4/11
 */
@Data
@Entity
@Table(name = "t_book_i18n")
public class Booki18n extends AuditorEntity{

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_data_id")
    private BookData bookData;

    @Column(name = "n_lang")
    private String lang;

    @Column(name = "n_name")
    private String name;

    @Column(name = "n_author")
    private String author;

    @Column(name = "n_description")
    private String description;

    @Column(name = "n_label")
    private String label;

    @Column(name = "n_catalogue")
    private String catalogue;
}
