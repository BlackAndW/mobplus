package com.yeecloud.adplus.admin.controller.app.vo;

import com.yeecloud.adplus.dal.entity.AdType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/1/28
 */

@Data
public class AppFunctionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private Integer functionCode;

    /** 应用版本 */
    private Integer appVersionId;

    /** 应用渠道 */
    private Integer channelId;

    /** 应用版本号 */
    private String versionCode;

    /** 应用渠道名 */
    private String channelName;

    private List<Object> adTypeList;

    private String remark;

    private Integer status;
}
