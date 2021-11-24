package com.yeecloud.adplus.admin.controller.app.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.app.vo.AppLinkVO;
import com.yeecloud.adplus.dal.entity.AppLink;
import com.yeecloud.adplus.dal.entity.AppLinkLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/7/16
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface AppLinkConvert {

    AppLinkVO convert(AppLink appLink);

    List<AppLinkVO> convert(List<AppLink> list);

    @Mappings({
            @Mapping(target = "id", source = "appLink.id"),
            @Mapping(target = "name", source = "appLink.name"),
            @Mapping(target = "imgPath", source = "appLink.imgPath"),
            @Mapping(target = "imgUrl", source = "appLink.imgUrl")
    })
    AppLinkVO convert(AppLinkLog appLinkLog);

    List<AppLinkVO> convert2(List<AppLinkLog> list);
}
