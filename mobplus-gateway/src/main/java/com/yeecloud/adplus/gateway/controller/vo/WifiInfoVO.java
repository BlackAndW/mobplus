package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Leonard
 * @create: 2021/6/24
 */
@Data
public class WifiInfoVO implements Serializable {
    private String name;
    private String gps;
    private String mac;
    private String type;
    private String password;
    private double distance;
}
