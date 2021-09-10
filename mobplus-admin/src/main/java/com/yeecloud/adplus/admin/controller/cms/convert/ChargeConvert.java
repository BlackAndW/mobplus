package com.yeecloud.adplus.admin.controller.cms.convert;

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
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface ChargeConvert {

    ChargeBannerVO convertBanner(ChargeBanner entity);

    List<ChargeBannerVO> convertBanner(List<ChargeBanner> list);

    @Mappings({
            @Mapping(target = "typeName", source = "type.name"),
    })
    ChargeVideoVO convertVideo(ChargeVideo entity);

    List<ChargeVideoVO> convertVideo(List<ChargeVideo> list);

    ChargeMTypeVO convertMType(ChargeMType entity);

    List<ChargeMTypeVO> convertMType(List<ChargeMType> list);

    @Mappings({
            @Mapping(target= "typeName", source = "type.name")
    })
    ChargeWallpaperVO convertWallpaper(ChargeWallpaper entity);

    List<ChargeWallpaperVO> convertWallpaper(List<ChargeWallpaper> list);
}
