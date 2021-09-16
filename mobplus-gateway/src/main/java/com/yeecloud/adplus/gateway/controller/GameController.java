package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apache.commons.beanutils.NewBeanUtils;
import com.google.common.collect.Lists;
import com.yeecloud.adplus.dal.entity.Game;
import com.yeecloud.adplus.dal.repository.GameRepository;
import com.yeecloud.adplus.gateway.controller.form.GameForm;
import com.yeecloud.adplus.gateway.controller.vo.GameVO;
import com.yeecloud.adplus.gateway.controller.vo.GameVO2;
import com.yeecloud.adplus.gateway.service.GameService;
import com.yeecloud.meeto.common.codec.Codec;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * H5游戏
 *
 * @author: Huang
 * @create: 2020-11-30 11:10
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/v1/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    GameRepository gameRepository;


//    @GetMapping("/banner")
//    public String getBannerGameList(@RequestBody(required = false) String body, @RequestParam(value = "m", required = false) String m) throws ServiceException {
//        boolean needCodec = m == null || m.trim().length() == 0;
//        if (body != null && needCodec) {
//            body = Codec.decode(body);
//        }
//        log.debug("ReqFromApp:{}", body);
//        List<Game> gameList = gameService.findGameList(9, 10);
//        return getGameVOList(needCodec, gameList);
//    }

//    @GetMapping
//    public String getGameList(@RequestBody(required = false) String body, @RequestParam(value = "m", required = false) String m) throws ServiceException {
//        boolean needCodec = m == null || m.trim().length() == 0;
//        if (body != null && needCodec) {
//            body = Codec.decode(body);
//        }
//        log.debug("ReqFromApp:{}", body);
//        List<Game> gameList = gameService.findGameList(1, 50);
//        return getGameVOList(needCodec, gameList);
//    }
//
//    private String getGameVOList(boolean needCodec, List<Game> gameList) {
//        List<GameVO> gameDataList = Lists.newArrayList();
//        for (Game game : gameList) {
//            GameVO data = new GameVO();
//            data.setGameId(game.getId());
//            data.setGameName(game.getName());
//            data.setGameDesc(game.getDesc());
//            data.setThumbUrl(game.getThumbUrl());
//            data.setGameUrl(game.getPlayUrl());
//            data.setStatus(game.getStatus());
//            gameDataList.add(data);
//        }
//        String response = JSON.toJSONString(Result.SUCCESS(gameDataList));
//        log.debug("RespToApp:{}", response);
//        return needCodec ? Codec.encode(response) : response;
//    }

    /**
     *
     * @return
     * @throws ServiceException
     */
    @ApiDoc
    @GetMapping("list")
    public Result<JSONObject> getGameListNew() throws ServiceException {
        List<Game> gameList = gameService.findGameListNew();
        JSONObject gameDataList = new JSONObject();
        JSONArray gameArray = new JSONArray();
        String curType = "";
        for (Game game : gameList) {
            if (curType.equals("")) {
                curType = game.getType();
            }
            GameVO2 vo2 = new GameVO2();
            NewBeanUtils.copyProperties(vo2, game, true);
            if (game.getType().equals(curType)) {
                gameArray.add(vo2);
            } else {
                gameDataList.put(curType, JSONArray.parseArray(gameArray.toJSONString()));
                gameArray.clear();
                curType = game.getType();
                gameArray.add(vo2);
            }
        }
        gameDataList.put(curType, gameArray);
        return Result.SUCCESS(gameDataList);
    }

    @PostMapping("update")
    public Result update(@RequestBody GameForm form) throws ServiceException {
        gameService.updateLogById(form);
        return Result.SUCCESS("game log updated");
    }

    // 导入游戏目录数据
    @RequestMapping("import")
    public void insertData() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("game.txt");
        int bytes;
        StringBuilder sb = new StringBuilder();
        while ((bytes = inputStream.read()) != -1) {
            sb.append((char) bytes);
        }
        String result  = sb.toString();
        JSONObject jsonObject = JSONObject.parseObject(result);
        String[] typeList = new String[] {"Puzzle", "Action", "Jump and Run", "Shooting", "Sports", "Racing", "Adventure", "Role playing", "Tower Defence"};
        System.out.println(typeList);
        save(typeList, jsonObject);
    }

    private void save(String[] typeList, JSONObject jsonObject) {
        for (String type : typeList) {
            List<String> nameList = (List<String>) jsonObject.get(type);
            for (String name: nameList) {
                Game game = new Game();
                game.setName(name);
                game.setType(type);
                game.setCreatedBy(1);
                game.setModifiedBy(1);
                gameRepository.save(game);
            }
        }
    }
}
