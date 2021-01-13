package com.yeecloud.adplus.admin.controller.release.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.release.vo.ChannelVO;
import com.yeecloud.adplus.dal.entity.Channel;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-01 15:03
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface ChannelConvert {
    ChannelVO convert(Channel entity);

    List<ChannelVO> convert(List<Channel> list);
}
