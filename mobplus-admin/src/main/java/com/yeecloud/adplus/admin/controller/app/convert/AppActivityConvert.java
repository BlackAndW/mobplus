package com.yeecloud.adplus.admin.controller.app.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.app.vo.AppActivityAwardVO;
import com.yeecloud.adplus.admin.controller.app.vo.AppActivityTaskVO;
import com.yeecloud.adplus.admin.controller.app.vo.AppActivityVO;
import com.yeecloud.adplus.dal.entity.AppActivity;
import com.yeecloud.adplus.dal.entity.AppActivityAward;
import com.yeecloud.adplus.dal.entity.AppActivityTask;
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

    AppActivityTaskVO convertTask(AppActivityTask appActivityTask);

    List<AppActivityTaskVO> convertTask(List<AppActivityTask> list);

    AppActivityAwardVO convertAward(AppActivityAward appActivityAward);

    List<AppActivityAwardVO> convertAward(List<AppActivityAward> list);
}
