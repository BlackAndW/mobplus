package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2022/4/11
 */
@Data
@Entity
@Table(name = "t_book_chapter_i18n")
public class BookChapteri18n extends AuditorEntity{

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_book_chapter_id")
    private BookChapter bookChapter;

    @Column(name = "n_chapter_no")
    private Integer chapterNo;

    @Column(name = "n_title")
    private String title;

    @Column(name = "n_content")
    private String content;

}
