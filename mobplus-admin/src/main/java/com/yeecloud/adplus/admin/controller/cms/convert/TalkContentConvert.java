package com.yeecloud.adplus.admin.controller.cms.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.cms.vo.TalkContentVO;
import com.yeecloud.adplus.dal.entity.TalkContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface TalkContentConvert {

    @Mappings({
            @Mapping(target = "talkId", source = "talk.id"),
    })
    TalkContentVO convert(TalkContent entity);

    List<TalkContentVO> convert(List<TalkContent> list);
}
