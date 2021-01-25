package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * @author: Leonard
 * @create: 2021/1/25
 */

@Data
public class AppActivityAwardVO implements Serializable {

    private static final long serialVersionUID= 1L;

    private Integer id;

//    /** 奖品类别 */
//    private Integer awardType;

    private String awardName;

    /** 奖品图片 */
    private String awardImgPath;

    /** 奖品碎片 */
    private String awardPiece;
}
