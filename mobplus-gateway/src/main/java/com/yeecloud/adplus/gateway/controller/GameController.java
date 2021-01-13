package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yeecloud.adplus.dal.entity.Game;
import com.yeecloud.adplus.gateway.controller.vo.GameVO;
import com.yeecloud.adplus.gateway.service.GameService;
import com.yeecloud.meeto.common.codec.Codec;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 游戏
 *
 * @author: Huang
 * @create: 2020-11-30 11:10
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/game")
public class GameController {


    @Autowired
    private GameService gameService;


    @GetMapping("/banner")
    public String getBannerGameList(@RequestBody(required = false) String body, @RequestParam(value = "m", required = false) String m) throws ServiceException {
        boolean needCodec = m == null || m.trim().length() == 0;
        if (body != null && needCodec) {
            body = Codec.decode(body);
        }
        log.debug("ReqFromApp:{}", body);
        List<Game> gameList = gameService.findGameList(9, 10);
        return getGameVOList(needCodec, gameList);
    }

    @GetMapping
    public String getGameList(@RequestBody(required = false) String body, @RequestParam(value = "m", required = false) String m) throws ServiceException {
        boolean needCodec = m == null || m.trim().length() == 0;
        if (body != null && needCodec) {
            body = Codec.decode(body);
        }
        log.debug("ReqFromApp:{}", body);
        List<Game> gameList = gameService.findGameList(1, 50);
        return getGameVOList(needCodec, gameList);
    }

    private String getGameVOList(boolean needCodec, List<Game> gameList) {
        List<GameVO> gameDataList = Lists.newArrayList();
        for (Game game : gameList) {
            GameVO data = new GameVO();
            data.setGameId(game.getId());
            data.setGameName(game.getName());
            data.setGameDesc(game.getDesc());
            data.setThumbUrl(game.getThumbUrl());
            data.setGameUrl(game.getPlayUrl());
            data.setStatus(game.getStatus());
            gameDataList.add(data);
        }
        String response = JSON.toJSONString(Result.SUCCESS(gameDataList));
        log.debug("RespToApp:{}", response);
        return needCodec ? Codec.encode(response) : response;
    }


}
