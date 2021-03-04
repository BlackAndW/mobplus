package com.yeecloud.adplus.admin.controller.app.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.ad.convert.AdPositionConvert;
import com.yeecloud.adplus.admin.controller.app.form.AppPositionAdPositionForm;
import com.yeecloud.adplus.admin.controller.app.vo.AppPositionAdPositionVO;
import com.yeecloud.adplus.dal.entity.AdPosition;
import com.yeecloud.adplus.dal.entity.AppPositionAdPosition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-08 17:46
 */
@Mapper(componentModel = "spring", uses = {AdPositionConvert.class, GeneralConvert.class})
public interface AppPosAdPosConvert {
    @Mappings({
            @Mapping(target = "id", source = "adPosition.id"),
            @Mapping(target = "adPos", source = "adPosition"),
            @Mapping(target = "ratio", source = "appPositionAdPosition.ratio"),
            @Mapping(target = "typeRatio", source = "appPositionAdPosition.typeRatio")
    })
    AppPositionAdPositionVO convert(AdPosition adPosition, AppPositionAdPosition appPositionAdPosition);

    @Mappings({
            @Mapping(target = "id", source = "adPosition.id"),
            @Mapping(target = "adPos", source = "adPosition")
    })
    AppPositionAdPositionVO convert(AppPositionAdPosition appPosAdPos);

    List<AppPositionAdPositionVO> convert(List<AppPositionAdPosition> list);

    @Mapping(target = "adPosition", source = "adPos")
    AppPositionAdPosition convert(AppPositionAdPositionForm form);
}
