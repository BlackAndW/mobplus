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
    private List<String> appVersionCheckList;

    /** 应用渠道 */
    private List<String> channelCheckList;

    private List<Object> adTypeList;

    private String remark;

    private Integer status;
}
