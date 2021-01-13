package com.yeecloud.adplus.admin.controller.ad.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.ad.vo.AdPositionVO;
import com.yeecloud.adplus.dal.entity.AdPosition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-08 16:30
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface AdPositionConvert {
    @Mappings({
            @Mapping(target = "advId", source = "advertiser.id"),
            @Mapping(target = "advName", source = "advertiser.name"),
            @Mapping(target = "appId", source = "app.id"),
            @Mapping(target = "appName", source = "app.name"),
            @Mapping(target = "type", source = "type.id"),
            @Mapping(target = "typeName", source = "type.name")
    })
    AdPositionVO convert(AdPosition adPosition);

    List<AdPositionVO> convert(List<AdPosition> list);
}
