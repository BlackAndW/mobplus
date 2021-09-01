package com.yeecloud.adplus.admin.controller.cms.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.cms.vo.GameVO;
import com.yeecloud.adplus.dal.entity.Game;
import com.yeecloud.adplus.dal.entity.GameLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Title
 *
 * Date: 2020-06-19 15:37:25
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface GameConvert {

    GameVO convert(Game entity);

    List<GameVO> convert(List<Game> list);

    @Mappings({
            @Mapping(target = "gameId", source = "game.id"),
            @Mapping(target = "name", source = "game.name"),
            @Mapping(target = "thumbUrl", source = "game.thumbUrl")
    })

    GameVO convert(GameLog entity);

    List<GameVO> convertLog(List<GameLog> list);

}
