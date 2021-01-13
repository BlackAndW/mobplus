package com.yeecloud.adplus.admin.controller.app.vo;

import com.yeecloud.adplus.admin.controller.ad.vo.AdPositionVO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Huang
 * @create: 2020-12-08 17:47
 */
@Data
public class AppPositionAdPositionVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private boolean checked = true;
    private AdPositionVO adPos;
    private Integer ratio;
    private String remark;

    public AppPositionAdPositionVO() {
    }

    public AppPositionAdPositionVO(Integer id, boolean checked, AdPositionVO adPos, Integer ratio) {
        this.id = id;
        this.checked = checked;
        this.adPos = adPos;
        this.ratio = ratio;
    }
}
