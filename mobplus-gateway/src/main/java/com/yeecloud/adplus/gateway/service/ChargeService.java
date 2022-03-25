package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.gateway.controller.form.ChargeSearchForm;
import com.yeecloud.adplus.gateway.controller.form.ChargeShowForm;
import com.yeecloud.adplus.gateway.controller.form.ChargeTypeForm;
import com.yeecloud.adplus.gateway.controller.vo.ChargeBannerVO;
import com.yeecloud.adplus.gateway.controller.vo.ChargeMTypeVO;
import com.yeecloud.adplus.gateway.controller.vo.ChargeMaterialVO;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.common.exception.ServiceException;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */
public interface ChargeService {

    List<ChargeBannerVO> queryBanner(ChargeShowForm form) throws ServiceException;

    List<ChargeMaterialVO> queryMaterial(ChargeShowForm form) throws ServiceException;

    Result uploadDataV(ChargeShowForm form);

    List<ChargeMTypeVO> queryTypeList(Integer style, String toLang, String appId) throws ServiceException;

    List<ChargeMTypeVO> queryTypeListNew(ChargeTypeForm form) throws ServiceException;

    List<ChargeMaterialVO> queryMaterialByLabel(ChargeSearchForm form) throws ServiceException;
}
