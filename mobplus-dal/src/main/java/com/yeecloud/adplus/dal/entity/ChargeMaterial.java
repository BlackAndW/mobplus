package com.yeecloud.adplus.dal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
@Entity
@Table(name = "t_charge_material")
public class ChargeMaterial extends AuditorEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_id")
    private App app;

    @Column(name = "n_name")
    private String name;

    /** 源视频地址 */
    @Column(name = "n_video_path")
    private String videoPath = "";

    /** 源视频名 */
    @Column(name = "n_video_name")
    private String videoName;

    /** 预览视频地址 */
    @Column(name = "n_video_introduce")
    private String videoIntroduce = "";

    /** 预览视频地址 */
    @Column(name = "n_video_introduce_name")
    private String videoIntroduceName;

    /** 视频封面地址 */
    @Column(name = "n_video_cover")
    private String videoCover = "";

    /** 视频封面缩略图地址 */
    @Column(name = "n_video_cover_thumb")
    private String videoCoverThumb = "";

    /** 壁纸原图 */
    @Column(name = "n_img_url")
    private String imgUrl = "";

    /** 壁纸缩略图 */
    @Column(name = "n_thumb_url")
    private String thumbUrl = "";

    /** 素材分类 */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_type_id")
    private ChargeMType type;

    /** 素材类型 1:视频 2:壁纸 */
    @Column(name = "n_style")
    private Integer style;

    /** 使用次数 */
    @Column(name = "n_use_num")
    private long useNum;

    /** 点击展示次数 */
    @Column(name = "n_show_num")
    private long showNum;

    /** 权重（排序用） */
    @Column(name = "n_weight")
    private long weight;

    /** 是否需要登录 */
    @Column(name = "n_use_limit")
    private Integer useLimit;

    @Transient
    private String useNumFake;

    public void fakeData() {
        long baseNum = this.getUseNum()/100;
        if (this.getUseNum() == 0) {
            this.setUseNumFake(new Random().nextInt(90) + 10 + "");
        } else if (baseNum == 0) {
            long useNumFake = this.getUseNum()/10 == 0 ? 100 : (this.getUseNum()/10)*1000;
            this.setUseNumFake(useNumFake + new Random().nextInt(999) + "");
        } else if (baseNum >= 1 && baseNum < 10) {
            DecimalFormat df = new DecimalFormat("0.0");
            this.setUseNumFake(df.format(this.getUseNum()/100f) + "W");
        } else if (baseNum >= 10) {
            this.setUseNumFake(baseNum + "W");
        }
    }

    public void updateWeight() {
        DecimalFormat df = new DecimalFormat("0.00");
        long useNum = this.getUseNum() == 0 ? 1 : this.getUseNum();
        long showNum = this.getShowNum() == 0 ? 1 : this.getShowNum();
        float div = Float.valueOf(df.format((float)useNum/showNum));
        // 计算和上传日期相隔的天数
        long cday = (System.currentTimeMillis() - this.getCreatedAt())/86400000;
        this.setWeight(useNum * 10 + (long)((div - cday)*1000));
    }
}
