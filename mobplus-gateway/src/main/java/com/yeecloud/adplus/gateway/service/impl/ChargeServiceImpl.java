package com.yeecloud.adplus.gateway.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.adplus.dal.repository.ChargeBannerRepository;
import com.yeecloud.adplus.dal.repository.ChargeMTypeRepository;
import com.yeecloud.adplus.dal.repository.ChargeMaterialRepository;
import com.yeecloud.adplus.gateway.controller.form.ChargeShowForm;
import com.yeecloud.adplus.gateway.controller.form.ChargeTypeForm;
import com.yeecloud.adplus.gateway.controller.form.TranslateForm;
import com.yeecloud.adplus.gateway.controller.vo.ChargeBannerVO;
import com.yeecloud.adplus.gateway.controller.vo.ChargeMTypeVO;
import com.yeecloud.adplus.gateway.controller.vo.ChargeMaterialVO;
import com.yeecloud.adplus.gateway.service.ChargeService;
import com.yeecloud.adplus.gateway.service.TranslateService;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/7/1
 */
@Service
public class ChargeServiceImpl implements ChargeService {

    @Autowired
    ChargeBannerRepository chargeBannerRepository;

    @Autowired
    ChargeMaterialRepository chargeMaterialRepository;

    @Autowired
    ChargeMTypeRepository chargeMTypeRepository;

    @Autowired
    TranslateService translateService;

    @Autowired
    AppRepository appRepository;

    @Override
    public List<ChargeBannerVO> queryBanner(ChargeShowForm form) throws ServiceException {
        QChargeBanner banner = QChargeBanner.chargeBanner;
        Predicate predicate = banner.deleted.eq(false);
        App app = appRepository.findByAppId(form.getAppId());
        if (app == null) {
            throw new ServiceException("app is not exist!");
        }
        predicate = ExpressionUtils.and(predicate, banner.app.appId.eq(form.getAppId()));
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "order"));
        List<ChargeBanner> bannerList = (List<ChargeBanner>) chargeBannerRepository.findAll(predicate, sort);
        List<ChargeBannerVO> bannerListVO = new ArrayList<>( bannerList.size() );
        for ( ChargeBanner chargeBanner : bannerList ) {
            ChargeBannerVO vo = new ChargeBannerVO();
            NewBeanUtils.copyProperties(vo, chargeBanner, true);
            bannerListVO.add(vo);
        }
        return bannerListVO;
    }

    @Override
    public List<ChargeMaterialVO> queryMaterial(ChargeShowForm form) throws ServiceException {
        QChargeMaterial material = QChargeMaterial.chargeMaterial;
        Predicate predicate = material.deleted.eq(false);
        Integer pageNo = form.getPageNo();
        if (pageNo == null || pageNo == 0) {
            pageNo = 0;
        }
        App app = appRepository.findByAppId(form.getAppId());
        if (app == null) {
            throw new ServiceException("app is not exist!");
        }
        // 默认获取视频，否则按style获取
        if (form.getStyle() == null) {
            throw new ServiceException("the value of style column is empty string!");
        }
        Integer type = form.getType();
        ChargeMType mType = chargeMTypeRepository.findById(type).orElse(null);
        if (mType == null) {
            throw new ServiceException("cannot find type by id");
        }
        String orderProperty = mType.getOrderColumn();
        int order = mType.getOrder();
        if (form.getIsAll() != null) {
            if (form.getIsAll() == 0) {
                predicate = ExpressionUtils.and(predicate, material.type.id.eq(type));
            } else if (form.getIsAll() != 1) {
                throw new ServiceException("the value of column isAll is illegal!");
            }
        } else {
            // 1,2,100,107的type为所有素材类型
            List<Integer> allStyle = Arrays.asList(1, 2, 100, 107);
            if (type > 0 && !allStyle.contains(type)) {
                predicate = ExpressionUtils.and(predicate, material.type.id.eq(type));
            }
        }
        predicate = ExpressionUtils.and(predicate, material.app.appId.eq(form.getAppId()));
        predicate = ExpressionUtils.and(predicate, material.style.eq(form.getStyle()));
        Sort.Direction direction = order == 1 ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(new Sort.Order(direction, orderProperty));
        PageRequest pageRequest = PageRequest.of(pageNo, 20, sort);
        List<ChargeMaterial> materialList = chargeMaterialRepository.findAll(predicate, pageRequest).getContent();
        return convertVideo(materialList);
    }

    private List<ChargeMaterialVO> convertVideo(List<ChargeMaterial> materialList) {
        materialList.forEach(ChargeMaterial::fakeData);
        List<ChargeMaterialVO> materialListVO = new ArrayList<>( materialList.size() );
        for ( ChargeMaterial chargeMaterial : materialList ) {
            ChargeMaterialVO vo = new ChargeMaterialVO();
            NewBeanUtils.copyProperties(vo, chargeMaterial, true);
            vo.setTypeId(chargeMaterial.getType().getId());
            vo.setTypeName(chargeMaterial.getType().getEnName());
            materialListVO.add(vo);
        }
        return materialListVO;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public synchronized Result uploadDataV(ChargeShowForm form) {
        if (form.getId() == null || form.getId() < 1) {
            return Result.FAILURE("id is null");
        }
        ChargeMaterial material = chargeMaterialRepository.getOne(form.getId());
        if (form.getShowNum() != null && form.getShowNum() > 0) {
            material.setShowNum(material.getShowNum() + 1);
        }
        if (form.getUseNum() != null && form.getUseNum() > 0) {
            material.setUseNum(material.getShowNum() + 1);
        }
        chargeMaterialRepository.save(material);
        return Result.SUCCESS("update material data success!");
    }

    @Override
    public List<ChargeMTypeVO> queryTypeList(Integer style, String toLang, String appId) throws ServiceException {
        QChargeMType type = QChargeMType.chargeMType;
        Predicate predicate = type.deleted.eq(false);
        App app = appRepository.findByAppId(appId);
        if (app == null) {
            throw new ServiceException("app is not exist!");
        }
        predicate = ExpressionUtils.and(predicate, type.app.appId.eq(appId));
        if (style != null && style > 0) {
            predicate = ExpressionUtils.and(predicate, type.style.eq(style));
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "rankOrder"));
            List<ChargeMType> resultList =   (List<ChargeMType>)chargeMTypeRepository.findAll(predicate, sort);
            if (toLang != null && toLang.length() > 0) {
                resultList.forEach(result -> {
//                    TranslateForm form = new TranslateForm(result.getEnName(), toLang);
//                    String newName = translateService.translation(form);
                    result.setEnName(result.getEnName());
                });
            }
            return convertMType(resultList);
        }
        return new ArrayList<>();
    }

    @Override
    public List<ChargeMTypeVO> queryTypeListNew(ChargeTypeForm form) throws ServiceException {
        QChargeMType type = QChargeMType.chargeMType;
        Predicate predicate = type.deleted.eq(false);
        App app = appRepository.findByAppId(form.getAppId());
        if (app == null) {
            throw new ServiceException("app is not exist!");
        }
        predicate = ExpressionUtils.and(predicate, type.app.appId.eq(form.getAppId()));
        if (form.getStyle() != null && form.getStyle() > 0) {
            predicate = ExpressionUtils.and(predicate, type.style.eq(form.getStyle()));
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "rankOrder"));
            List<ChargeMType> resultList =   (List<ChargeMType>)chargeMTypeRepository.findAll(predicate, sort);
            if (form.getToLang() != null && form.getToLang().length() > 0) {
                resultList.forEach(result -> {
//                    TranslateForm form = new TranslateForm(result.getEnName(), toLang);
//                    String newName = translateService.translation(form);
                    result.setEnName(result.getEnName());
                });
            }
            return convertMType(resultList);
        }
        return new ArrayList<>();
    }

    private List<ChargeMTypeVO> convertMType(List<ChargeMType> resultList) {
        List<ChargeMTypeVO> mTypeVOS = new ArrayList<>( resultList.size() );
        for ( ChargeMType chargeMType : resultList ) {
            ChargeMTypeVO vo = new ChargeMTypeVO();
            NewBeanUtils.copyProperties(vo, chargeMType, true);
            mTypeVOS.add(vo);
        }
        return mTypeVOS;
    }
}
