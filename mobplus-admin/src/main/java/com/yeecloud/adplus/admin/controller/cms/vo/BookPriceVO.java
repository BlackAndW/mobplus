package com.yeecloud.adplus.admin.controller.cms.vo;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2022/2/18
 */
@Data
public class BookPriceVO {

    private static final long serialVersionUID = 1L;

    private Integer pricePer1000 = 1;

    private float vipDiscount = 8;
}
