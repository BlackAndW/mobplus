package com.yeecloud.adplus.admin.controller.data.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.data.vo.UserAdInfoVO;
import com.yeecloud.adplus.dal.entity.UserAdInfo;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/5/18
 */

@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface UserAdInfoConvert {
    UserAdInfoVO convert(UserAdInfo app);

    List<UserAdInfoVO> convert(List<UserAdInfo> list);
}
