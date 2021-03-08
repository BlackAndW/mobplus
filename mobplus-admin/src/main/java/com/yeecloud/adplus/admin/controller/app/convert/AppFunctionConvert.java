package com.yeecloud.adplus.admin.controller.app.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.app.vo.AppFunctionVO;
import com.yeecloud.adplus.dal.entity.AppFunction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/1/28
 */

@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface AppFunctionConvert {

    @Mappings({
            @Mapping(target = "adTypeList", source = "adTypeConf")
    })

    AppFunctionVO convert(AppFunction appActivity);

    List<AppFunctionVO> convert(List<AppFunction> list);
}
