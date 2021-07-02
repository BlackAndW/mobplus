package com.yeecloud.adplus.admin.controller.cms.vo;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */
@Data
public class ChargeMTypeVO {

    private Integer id;

    private String name;

    private String orderColumn;

    private Integer orderRule;

    private Integer rankOrder;
}
