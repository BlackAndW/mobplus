package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.cms.form.ChargeBannerForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeMTypeForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeMaterialForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeMaterialForm;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

import java.util.List;

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

    Page<ChargeMaterial> queryMaterial(Query query) throws ServiceException;

    ChargeMaterial findMaterialById(Integer id) throws ServiceException;

    void createMaterial(ChargeMaterialForm form) throws ServiceException;

    void updateMaterial(Integer id, ChargeMaterialForm form) throws ServiceException;

    void deleteMaterial(Integer[] ids) throws ServiceException;

    Page<ChargeMType> queryMType(Query query) throws ServiceException;

    ChargeMType findMTypeById(Integer id) throws ServiceException;

    void createMType(ChargeMTypeForm form) throws ServiceException;

    void updateMType(Integer id, ChargeMTypeForm form) throws ServiceException;

    void deleteMType(Integer[] ids) throws ServiceException;

    void updateWeight();

    boolean checkMaterialByName(String name);

    List<App> queryWallpaperAppList() throws ServiceException;
}
