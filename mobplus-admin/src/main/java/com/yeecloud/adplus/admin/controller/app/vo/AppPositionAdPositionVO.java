package com.yeecloud.adplus.admin.controller.app.vo;

import com.yeecloud.adplus.admin.controller.ad.vo.AdPositionVO;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author: Huang
 * @create: 2020-12-08 17:47
 */
@Data
public class AppPositionAdPositionVO implements Serializable, Comparable<AppPositionAdPositionVO> {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private boolean checked = true;
    private AdPositionVO adPos;
    private Integer ratio = 1;
    private Boolean ratioFlag = false;
    private Integer typeRatio = 1;
    private Integer limitShowCount = 3;
    private Integer limitClickCount = 2;
    private String remark;

    public AppPositionAdPositionVO() {
    }

    public AppPositionAdPositionVO(Integer id, boolean checked, AdPositionVO adPos) {
        this.id = id;
        this.checked = checked;
        this.adPos = adPos;
    }

    /** 按广告平台id升序排序 */
    @Override
    public int compareTo(@NotNull AppPositionAdPositionVO o) {
        if (adPos == null) {
            return -1;
        }
        if (adPos.getAdvId() > o.getAdPos().getAdvId()) {
            return 1;
        } else if (adPos.getAdvId().equals(o.getAdPos().getAdvId())) {
            return 0;
        } else {
            return -1;
        }
    }
}
