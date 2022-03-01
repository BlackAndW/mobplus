package com.yeecloud.adplus.admin.controller.cms.form;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2022/2/17
 */
@Data
public class BookPriceForm {

    private Integer appId;

    private Integer pricePer1000;

    private float vipDiscount;
}
