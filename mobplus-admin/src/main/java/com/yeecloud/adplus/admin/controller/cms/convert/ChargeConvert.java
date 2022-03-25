package com.yeecloud.adplus.admin.controller.cms.convert;

import com.yeecloud.adplus.admin.common.convert.ConvertWorker;
import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.cms.vo.*;
import com.yeecloud.adplus.dal.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/30
 */
@Mapper(componentModel = "spring", uses = {GeneralConvert.class, ConvertWorker.class})
public interface ChargeConvert {

    ChargeBannerVO convertBanner(ChargeBanner entity);

    List<ChargeBannerVO> convertBanner(List<ChargeBanner> list);

    @Mappings({
            @Mapping(target = "typeId", source = "type.id"),
            @Mapping(target = "typeName", source = "type.name"),
            @Mapping(target = "labels", source = "labels", qualifiedByName = "str2list")
    })
    ChargeMaterialVO convertMaterial(ChargeMaterial entity);

    List<ChargeMaterialVO> convertMaterial(List<ChargeMaterial> list);

    ChargeMTypeVO convertMType(ChargeMType entity);

    List<ChargeMTypeVO> convertMType(List<ChargeMType> list);

    ChargeLabelVO convertLabel(ChargeLabel entity);

    List<ChargeLabelVO> convertLabel(List<ChargeLabel> list);

}
