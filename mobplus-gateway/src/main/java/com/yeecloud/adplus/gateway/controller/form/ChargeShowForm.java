package com.yeecloud.adplus.gateway.controller.form;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/7/2
 */
@Data
public class ChargeShowForm {

    /** 页码 起始页为0 */
    private Integer pageNo;

    /** 类型id */
    private Integer type;

    /** 素材id */
    private Integer id;

    /** 使用次数 传0或1 */
    private Integer useNum;

    /** 展示次数 传0或1 */
    private Integer showNum;

    /** 素材类型 1:视频(默认), 2:壁纸 */
    private Integer style;

}
