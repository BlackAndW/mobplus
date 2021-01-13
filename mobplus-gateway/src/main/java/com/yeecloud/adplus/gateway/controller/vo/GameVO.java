package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Huang
 * @create: 2020-11-30 11:15
 */
@Data
public class GameVO implements Serializable {
    private Integer gameId;
    private String gameName;
    private String gameDesc;
    private String thumbUrl;
    private String gameUrl;
    private Integer status;

    public GameVO() {

    }

    public GameVO(Integer gameId, String gameName, String gameDesc, String thumbUrl, String gameUrl, Integer status) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameDesc = gameDesc;
        this.thumbUrl = thumbUrl;
        this.gameUrl = gameUrl;
        this.status = status;
    }
}
