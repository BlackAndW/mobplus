package com.yeecloud.adplus.admin.controller.app.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.app.vo.AppActivityVO;
import com.yeecloud.adplus.dal.entity.AppActivity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/1/18
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface AppActivityConvert {

    AppActivityVO convert(AppActivity appActivity);

    List<AppActivityVO> convert(List<AppActivity> list);
}
