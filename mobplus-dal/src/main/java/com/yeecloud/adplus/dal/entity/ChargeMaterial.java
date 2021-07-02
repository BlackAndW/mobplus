package com.yeecloud.adplus.dal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
@Entity
@Table(name = "t_charge_material")
public class ChargeMaterial extends AuditorEntity {

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "n_name")
    private String name;

    /** 素材分类 */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_type_id")
    private ChargeMType type;

    /** 源素材地址 */
    @Column(name = "n_video_path")
    private String videoPath;

    /** 源素材名 */
    @Column(name = "n_video_name")
    private String videoName;

    /** 预览素材地址 */
    @Column(name = "n_video_introduce")
    private String videoIntroduce;

    /** 预览素材地址 */
    @Column(name = "n_video_introduce_name")
    private String videoIntroduceName;

    /** 素材封面地址 */
    @Column(name = "n_video_cover")
    private String videoCover;

    /** 使用次数 */
    @Column(name = "n_use_num")
    private long useNum;

    /** 点击展示次数 */
    @Column(name = "n_show_num")
    private long showNum;

    /** 权重（排序用） */
    @Column(name = "n_weight")
    private Integer weight;

    /** 是否收藏 */
    @Column(name = "n_collection")
    private Integer collection;

    /** 是够需要登录 */
    @Column(name = "n_use_limit")
    private Integer useLimit;
}
