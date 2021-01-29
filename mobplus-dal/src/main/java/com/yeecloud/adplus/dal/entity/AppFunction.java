package com.yeecloud.adplus.dal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/1/28
 */

@Data
@Entity
@Table(name = "t_app_function")
public class AppFunction extends AuditorEntity{

    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 所属应用 */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_id")
    private App app;

    /** 任务名称 */
    @Column(name = "c_name")
    private String name;

    /** 广告类型 */
    @Column(name = "n_type")
    private String adTypeConf;

    @Column(name = "c_remark")
    private String remark;

    @Column(name = "n_status")
    private Integer status;

}
