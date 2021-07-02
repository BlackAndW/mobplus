package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.dal.entity.ChargeBanner;
import com.yeecloud.adplus.dal.entity.ChargeMType;
import com.yeecloud.adplus.dal.entity.ChargeMaterial;
import com.yeecloud.adplus.gateway.controller.form.ChargeShowForm;
import com.yeecloud.adplus.gateway.controller.vo.ChargeBannerVO;
import com.yeecloud.adplus.gateway.controller.vo.ChargeMaterialVO;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.Query;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */
public interface ChargeService {

    List<ChargeBannerVO> queryBanner() throws ServiceException;

    ChargeBanner findBannerById(Integer id) throws ServiceException;

    List<ChargeMaterialVO> queryMaterial(Query query) throws ServiceException;

    ChargeMaterial findMaterialById(Integer id) throws ServiceException;

    List<ChargeMType> queryMType() throws ServiceException;

    ChargeMType findMTypeById(Integer id) throws ServiceException;

    Result uploadData(ChargeShowForm form);
}
