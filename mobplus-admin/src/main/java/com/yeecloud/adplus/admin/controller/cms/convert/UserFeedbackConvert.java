package com.yeecloud.adplus.admin.controller.cms.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.cms.vo.UserFeedbackVO;
import com.yeecloud.adplus.dal.entity.UserFeedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Title
 *
 * Date: 2020-06-19 15:37:25
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface UserFeedbackConvert {

    @Mappings({
            @Mapping(target = "appId", source = "app.id"),
            @Mapping(target = "appName", source = "app.name")
    })
    UserFeedbackVO convert(UserFeedback entity);

    List<UserFeedbackVO> convert(List<UserFeedback> list);

}
