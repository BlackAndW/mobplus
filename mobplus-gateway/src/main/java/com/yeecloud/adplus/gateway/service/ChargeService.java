package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.dal.entity.ChargeBanner;
import com.yeecloud.adplus.dal.entity.ChargeMType;
import com.yeecloud.adplus.dal.entity.ChargeVideo;
import com.yeecloud.adplus.dal.entity.ChargeWallpaper;
import com.yeecloud.adplus.gateway.controller.form.ChargeShowForm;
import com.yeecloud.adplus.gateway.controller.vo.ChargeBannerVO;
import com.yeecloud.adplus.gateway.controller.vo.ChargeMTypeVO;
import com.yeecloud.adplus.gateway.controller.vo.ChargeVideoVO;
import com.yeecloud.adplus.gateway.controller.vo.ChargeWallpaperVO;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */
public interface ChargeService {

    List<ChargeBannerVO> queryBanner() throws ServiceException;

    List<ChargeVideoVO> queryVideo(ChargeShowForm form) throws ServiceException;

    List<ChargeWallpaperVO> queryWallpaper(ChargeShowForm form) throws ServiceException;

    Result uploadDataV(ChargeShowForm form);

    Result uploadDataWP(ChargeShowForm form);

    List<ChargeMTypeVO> queryTypeList(Integer style);
}
