package com.yeecloud.adplus.admin.controller.ad.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.ad.vo.AdvertiserVO;
import com.yeecloud.adplus.dal.entity.Advertiser;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-08 15:39
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface AdvertiserConvert {
    AdvertiserVO convert(Advertiser advertiser);

    List<AdvertiserVO> convert(List<Advertiser> list);
}

