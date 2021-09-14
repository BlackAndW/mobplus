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

    /** video id */
    private Integer vid;

    /** 壁纸 id */
    private Integer wpid;

    /** 使用次数 传0或1 */
    private Integer useNum;

    /** 展示次数 传0或1 */
    private Integer showNum;

}
