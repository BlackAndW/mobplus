package com.yeecloud.adplus.dal.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_book_price")
public class BookPrice extends AuditorEntity{

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

    @Column(name = "n_price_per_1000")
    private long pricePer1000;

    @Column(name = "n_vip_discount")
    private float vipDiscount;
}
