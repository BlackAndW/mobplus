package com.yeecloud.adplus.admin.controller.cms.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.cms.vo.TalkCategoryVO;
import com.yeecloud.adplus.dal.entity.TalkCategory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface TalkCategoryConvert {


    TalkCategoryVO convert(TalkCategory entity);

    List<TalkCategoryVO> convert(List<TalkCategory> list);
}
