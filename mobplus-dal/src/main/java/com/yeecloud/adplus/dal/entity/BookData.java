package com.yeecloud.adplus.dal.entity;

import lombok.Data;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;
import java.util.Random;

@Data
@Entity
@Table(name = "t_book_data")
public class BookData extends AuditorEntity{

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_id")
    private App app;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_channel_id")
    private BookChannel bookChannel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_type_id")
    private BookType bookType;

    @Column(name = "n_type_org")
    private String typeOrg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_sex_id")
    private BookSex bookSex;

    @Column(name = "n_label_ids")
    private String labels;

    @Column(name = "n_name")
    private String name;

    @Column(name = "n_author")
    private String author;

    @Column(name = "n_cover")
    private String cover;

    @Column(name = "n_description")
    private String description;

    @Column(name = "n_status")
    private Integer status;

    @Column(name = "n_is_online")
    private Integer isOnline;

    @Column(name = "n_is_vip")
    private Integer isVip;

    @Column(name = "n_is_free")
    private Integer isFree;

    @Column(name = "n_size")
    private long size;

    @Column(name = "n_read_count")
    private long readCount;

    @Transient
    private long fakeReadCount;

    @Column(name = "n_user_count")
    private long userCount;

    @Column(name = "n_collection")
    private long collection;

    @Column(name = "n_lock_count")
    private long lockCount;

    public void fakeData() {
        if (this.getReadCount() == 0) {
            this.setFakeReadCount(new Random().nextInt(900) + 100);
        } else if (this.getReadCount() <= 100) {
            this.setFakeReadCount(this.getReadCount() + 1000);
        } else if (this.getReadCount() > 100) {
            this.setFakeReadCount(this.getReadCount() * 5 + 1000);
        }
    }
}
