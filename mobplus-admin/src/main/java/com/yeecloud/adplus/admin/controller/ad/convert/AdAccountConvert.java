package com.yeecloud.adplus.admin.controller.ad.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.ad.vo.AdAccountVO;
import com.yeecloud.adplus.dal.entity.AdAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/10
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface AdAccountConvert{
    @Mappings({
            @Mapping(target = "advId", source = "advertiser.id")
    })
    AdAccountVO convert(AdAccount adAccount);

    List<AdAccountVO> convert(List<AdAccount> list);
}
