package com.yeecloud.adplus.admin.controller.app.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.app.vo.AppProjectVO;
import com.yeecloud.adplus.dal.entity.AppProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-02 16:24
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface AppProjectConvert {
    @Mappings({
            @Mapping(target = "appId", source = "app.id"),
            @Mapping(target = "appVersionId", source = "appVersion.id"),
            @Mapping(target = "appVersionCode", source = "appVersion.code"),
            @Mapping(target = "channelId", source = "channel.id"),
            @Mapping(target = "channelName", source = "channel.name"),
    })
    AppProjectVO convert(AppProject entity);

    List<AppProjectVO> convert(List<AppProject> list);
}


