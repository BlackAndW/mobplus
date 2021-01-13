package com.yeecloud.adplus.admin.controller.app.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.app.vo.AppVO;
import com.yeecloud.adplus.dal.entity.App;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-02 11:04
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface AppConvert {

    @Mappings({
            @Mapping(target = "extra", source = "conf"),
    })
    AppVO convert(App app);

    List<AppVO> convert(List<App> list);
}
