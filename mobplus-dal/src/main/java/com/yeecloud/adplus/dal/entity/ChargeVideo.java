package com.yeecloud.adplus.dal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
@Entity
@Table(name = "t_charge_video")
public class ChargeVideo extends ChargeMaterial {

    /** 源视频地址 */
    @Column(name = "n_video_path")
    private String videoPath;

    /** 源视频名 */
    @Column(name = "n_video_name")
    private String videoName;

    /** 预览视频地址 */
    @Column(name = "n_video_introduce")
    private String videoIntroduce;

    /** 预览视频地址 */
    @Column(name = "n_video_introduce_name")
    private String videoIntroduceName;

    /** 视频封面地址 */
    @Column(name = "n_video_cover")
    private String videoCover;

    /** 视频封面缩略图地址 */
    @Column(name = "n_video_cover_thumb")
    private String videoCoverThumb;

}
