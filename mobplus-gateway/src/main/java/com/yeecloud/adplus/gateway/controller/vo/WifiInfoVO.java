package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Leonard
 * @create: 2021/6/24
 */
@Data
public class WifiInfoVO implements Serializable {

    /** wifi名 */
    private String name;

    /** gps坐标 */
    private String gps;

    /** mac地址 */
    private String mac;

    /** 类型 */
    private String type;

    /** 密码 */
    private String password;

    /** 搜索范围 */
    private double distance;
}
