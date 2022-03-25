package com.yeecloud.adplus.dal.entity;

import lombok.Data;
import org.hibernate.cache.spi.support.NaturalIdNonStrictReadWriteAccess;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2022/3/23
 */
@Data
@Entity
@Table(name = "t_charge_label")
public class ChargeLabel extends AuditorEntity{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "n_name")
    private String name;

    @Column(name = "n_en_name")
    private String enName;

    @Column(name = "n_type")
    private Integer type;

}
