package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Random;

@Data
@Entity
@Table(name = "t_book_chapter")
public class BookChapter extends AuditorEntity{

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_book_id")
    private BookData bookData;

    @Column(name = "n_name")
    private String name;

    @Column(name = "n_content")
    private String content;

    @Column(name = "n_chapter_no")
    private Integer chapterNo;

    @Column(name = "n_read_count")
    private long readCount;

    @Transient
    private long fakeReadCount;

    @Column(name = "n_exit_count")
    private long exitCount;

    @Transient
    private Integer exitPer;

    @Column(name = "n_is_lock")
    private Integer isLock;

    @Column(name = "n_lock_count")
    private Integer lockCount;

    public void fakeData() {
        if (this.getReadCount() == 0) {
            this.setFakeReadCount(new Random().nextInt(900) + 100);
        } else if (this.getReadCount() <= 100) {
            this.setFakeReadCount(this.getReadCount() + 1000);
        } else if (this.getReadCount() > 100) {
            this.setFakeReadCount(this.getReadCount() * 5 + 1000);
        }
    }

    public void calExitPer() {
        BigDecimal exitCount = new BigDecimal(this.getExitCount());
        BigDecimal readCount = new BigDecimal(this.getReadCount());
        BigDecimal result = exitCount.divide(readCount, 2, BigDecimal.ROUND_HALF_UP);
        this.setExitPer(result.multiply(new BigDecimal(100)).intValue());
    }
}
