package com.yeecloud.adplus.admin.controller.app.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.app.vo.AppConfigVO;
import com.yeecloud.adplus.dal.entity.AppConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-02 17:41
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface AppConfigConvert {

    @Mappings({
            @Mapping(target = "appId", source = "app.id"),
            @Mapping(target = "appProjectId", source = "appProject.id"),
            @Mapping(target = "appVersionId", source = "appVersion.id"),
            @Mapping(target = "channelId", source = "channel.id"),
    })
    AppConfigVO convert(AppConfig appConfig);

    List<AppConfigVO> convert(List<AppConfig> list);
}
