package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/8/26
 */
@Data
public class ScannerVO {

    /** 结果名 */
    private String name;

    /** 识别准确率 */
    private BigDecimal score;

    /** 结果描述 */
    private String des;

    /** wiki封面url */
    private String imgUrl;

    /** 百度+wiki封面 **/
    private List<String> imgUrlList;
}
