package com.yeecloud.adplus.dal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/9/8
 */

@Data
@Entity
@Table(name = "t_charge_wallpaper")
public class ChargeWallpaper extends ChargeMaterial {

    /** 壁纸原图 */
    @Column(name = "n_img_url")
    private String imgUrl;

    /** 壁纸缩略图 */
    @Column(name = "n_thumb_url")
    private String thumbUrl;

}
