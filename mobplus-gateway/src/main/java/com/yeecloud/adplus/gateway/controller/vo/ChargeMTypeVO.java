package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

/**
 * 类型
 * @author: Leonard
 * @create: 2021/9/10
 */
@Data
public class ChargeMTypeVO {

    /** 类型id */
    private Integer id;

    /** 类型名 */
    private String name;

    /** 英文名 */
    private String enName;

    /** 排序根据 例如：日期，权重 */
    private String orderColumn;
}
