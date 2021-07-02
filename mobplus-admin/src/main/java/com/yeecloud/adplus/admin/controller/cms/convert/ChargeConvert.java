package com.yeecloud.adplus.admin.controller.cms.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.cms.vo.ChargeBannerVO;
import com.yeecloud.adplus.admin.controller.cms.vo.ChargeMTypeVO;
import com.yeecloud.adplus.admin.controller.cms.vo.ChargeMaterialVO;
import com.yeecloud.adplus.admin.controller.cms.vo.GameVO;
import com.yeecloud.adplus.dal.entity.ChargeBanner;
import com.yeecloud.adplus.dal.entity.ChargeMType;
import com.yeecloud.adplus.dal.entity.ChargeMaterial;
import com.yeecloud.adplus.dal.entity.Game;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/30
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface ChargeConvert {

    ChargeBannerVO convertBanner(ChargeBanner entity);

    List<ChargeBannerVO> convertBanner(List<ChargeBanner> list);

    @Mappings({
            @Mapping(target = "type", source = "type.name"),
    })
    ChargeMaterialVO convertMaterial(ChargeMaterial entity);

    List<ChargeMaterialVO> convertMaterial(List<ChargeMaterial> list);

    ChargeMTypeVO convertMType(ChargeMType entity);

    List<ChargeMTypeVO> convertMType(List<ChargeMType> list);
}
