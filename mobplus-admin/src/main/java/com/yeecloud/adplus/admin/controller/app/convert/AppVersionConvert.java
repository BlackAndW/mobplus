package com.yeecloud.adplus.admin.controller.app.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.app.vo.AppVersionVO;
import com.yeecloud.adplus.dal.entity.AppVersion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-02 14:51
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface AppVersionConvert {
    @Mappings({
            @Mapping(target = "appId", source = "app.id"),
    })
    AppVersionVO convert(AppVersion appVersion);

    List<AppVersionVO> convert(List<AppVersion> list);
}
