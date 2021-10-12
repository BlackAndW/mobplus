package com.yeecloud.adplus.gateway.controller.form;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/8/31
 */
@Data
public class GameForm {

    /** ---游戏id */
    private Integer id;

    /** ---点击数 ---传0或1 */
    private Integer clickNum;

    /** ---访问人(ip)数 ---传0或1 */
    private Integer ipNum;

    /** ---游玩时间 ---单位：秒/s */
    private Integer playTime;
}
