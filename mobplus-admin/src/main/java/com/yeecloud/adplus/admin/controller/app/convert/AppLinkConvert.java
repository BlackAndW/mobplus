package com.yeecloud.adplus.admin.controller.app.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.app.vo.AppLinkVO;
import com.yeecloud.adplus.dal.entity.AppLink;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/7/16
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface AppLinkConvert {

    AppLinkVO convert(AppLink appLink);

    List<AppLinkVO> convert(List<AppLink> list);
}
