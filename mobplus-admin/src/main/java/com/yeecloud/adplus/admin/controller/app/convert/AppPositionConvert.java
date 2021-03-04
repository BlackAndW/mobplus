package com.yeecloud.adplus.admin.controller.app.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.app.vo.AppPositionVO;
import com.yeecloud.adplus.dal.entity.AppPosition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-08 17:58
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface AppPositionConvert {
    @Mappings({
            @Mapping(target = "appId", source = "app.id"),
            @Mapping(target = "appName", source = "app.name")
//            @Mapping(target = "type", source = "type.id"),
//            @Mapping(target = "typeName", source = "type.name")
//            @Mapping(target = "type", ignore = true)
    })
    AppPositionVO convert(AppPosition appPosition);

    List<AppPositionVO> convert(List<AppPosition> list);
}
