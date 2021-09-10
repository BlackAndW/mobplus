package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.cms.form.ChargeBannerForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeMTypeForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeVideoForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeWallpaperForm;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */
public interface ChargeService{

    Page<ChargeBanner> queryBanner(Query query) throws ServiceException;

    ChargeBanner findBannerById(Integer id) throws ServiceException;

    void createBanner(ChargeBannerForm form) throws ServiceException;

    void updateBanner(Integer id, ChargeBannerForm form) throws ServiceException;

    void deleteBanner(Integer[] ids) throws ServiceException;

    Page<ChargeVideo> queryVideo(Query query) throws ServiceException;

    ChargeVideo findVideoById(Integer id) throws ServiceException;

    void createVideo(ChargeVideoForm form) throws ServiceException;

    void updateVideo(Integer id, ChargeVideoForm form) throws ServiceException;

    void deleteVideo(Integer[] ids) throws ServiceException;

    Page<ChargeWallpaper> queryWallpaper(Query query) throws ServiceException;

    ChargeWallpaper findWallpaperById(Integer id) throws ServiceException;

    void createWallpaper(ChargeWallpaperForm form) throws ServiceException;

    void updateWallpaper(Integer id, ChargeWallpaperForm form) throws ServiceException;

    void deleteWallpaper(Integer[] ids) throws ServiceException;

    Page<ChargeMType> queryMType(Query query) throws ServiceException;

    ChargeMType findMTypeById(Integer id) throws ServiceException;

    void createMType(ChargeMTypeForm form) throws ServiceException;

    void updateMType(Integer id, ChargeMTypeForm form) throws ServiceException;

    void deleteMType(Integer[] ids) throws ServiceException;

    void updateWeight();

    boolean checkVideoByName(String name);
}
