package com.yeecloud.adplus.admin.controller.app.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.app.vo.AppMobileConfVO;
import com.yeecloud.adplus.dal.entity.AppMobileConf;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-15 09:46
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface AppMobileConfConvert {

    @Mappings({
            @Mapping(target = "appId", source = "app.id"),
    })
    AppMobileConfVO convert(AppMobileConf appMobileConf);

    List<AppMobileConfVO> convert(List<AppMobileConf> list);
}
