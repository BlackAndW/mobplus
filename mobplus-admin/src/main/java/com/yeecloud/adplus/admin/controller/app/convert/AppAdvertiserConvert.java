package com.yeecloud.adplus.admin.controller.app.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.app.vo.AppAdvertiserVO;
import com.yeecloud.adplus.dal.entity.AppAdvertiser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-09 11:00
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface AppAdvertiserConvert {
    @Mappings({
            @Mapping(target = "appAppId", source = "app.id"),
            @Mapping(target = "advId", source = "advertiser.id"),
            @Mapping(target = "advName", source = "advertiser.name"),
    })
    AppAdvertiserVO convert(AppAdvertiser appAdvertiser);

    List<AppAdvertiserVO> convert(List<AppAdvertiser> list);
}
