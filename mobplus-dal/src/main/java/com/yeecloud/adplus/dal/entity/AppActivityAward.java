package com.yeecloud.adplus.dal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/1/18
 */
@Data
@Entity
@Table(name = "t_app_activity_award")
public class AppActivityAward extends AuditorEntity{

    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 所属活动 */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_activity_id")
    private AppActivity appActivity;

    /** 奖品类别 */
    @Column(name = "award_type")
    private Integer awardType;

    /** 奖品名称 */
    @Column(name = "award_name")
    private String awardName;

    /** 奖品图片 */
    @Column(name = "award_img_path")
    private String awardImgPath;

    /** 奖品碎片 */
    @Column(name = "award_piece")
    private String awardPiece;

    @Column(name = "c_remark")
    private String remark;

    @Column(name = "n_status")
    private Integer status;
}
