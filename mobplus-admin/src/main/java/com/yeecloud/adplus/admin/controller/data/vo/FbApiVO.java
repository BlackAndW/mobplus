package com.yeecloud.adplus.admin.controller.data.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Leonard
 * @create: 2021/7/14
 */
@Data
public class FbApiVO implements Serializable {

    public static final long serialVersionUID = 1L;

    private String name;

    // 成效
    private String result;

    // 展示次数
    private String impressions;

    // 单次成效费用
    private String cost_per_result;

    // 花费金额
    private String spend;

    // 点击量
    private String clicks;

//    private String dateStart;
//
//    private String dateStop;
}
