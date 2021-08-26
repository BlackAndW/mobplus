package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: Leonard
 * @create: 2021/8/26
 */
@Data
public class ScannerVO {

    private String name;

    private BigDecimal score;

    private String des;
}
