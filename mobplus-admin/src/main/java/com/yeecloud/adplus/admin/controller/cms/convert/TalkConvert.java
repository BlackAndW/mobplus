package com.yeecloud.adplus.admin.controller.cms.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.cms.vo.TalkVO;
import com.yeecloud.adplus.dal.entity.Talk;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface TalkConvert {

    @Mappings({
            @Mapping(target = "categoryId", source = "category.id"),
    })
    TalkVO convert(Talk entity);

    List<TalkVO> convert(List<Talk> list);
}
