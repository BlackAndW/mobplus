package com.yeecloud.adplus.gateway.service.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.ChargeBannerRepository;
import com.yeecloud.adplus.dal.repository.ChargeMTypeRepository;
import com.yeecloud.adplus.dal.repository.ChargeMaterialRepository;
import com.yeecloud.adplus.gateway.controller.form.ChargeShowForm;
import com.yeecloud.adplus.gateway.controller.vo.ChargeBannerVO;
import com.yeecloud.adplus.gateway.controller.vo.ChargeMaterialVO;
import com.yeecloud.adplus.gateway.service.ChargeService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.Query;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @Override
    public List<ChargeBannerVO> queryBanner() throws ServiceException {
        QChargeBanner banner = QChargeBanner.chargeBanner;
        Predicate predicate = banner.deleted.eq(false);
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "order"));
        List<ChargeBanner> bannerList = (List<ChargeBanner>) chargeBannerRepository.findAll(predicate, sort);
        List<ChargeBannerVO> bannerListVO = new ArrayList<>( bannerList.size() );
        for ( ChargeBanner chargeBanner : bannerList ) {
            ChargeBannerVO vo = new ChargeBannerVO();
            BeanUtils.copyProperties(chargeBanner, vo);
            bannerListVO.add(vo);
        }
        return bannerListVO;
    }

    @Override
    public ChargeBanner findBannerById(Integer id) throws ServiceException {
        return null;
    }

    @Override
    public List<ChargeMaterialVO> queryMaterial(Query query) throws ServiceException {
        QChargeMaterial material = QChargeMaterial.chargeMaterial;
        Predicate predicate = material.deleted.eq(false);

        Integer pageNo = query.get("pageNo", Integer.class);
        if (pageNo == null || pageNo == 0) {
            pageNo = 0;
        }
//        Integer collection = query.get("collection", Integer.class);
//        if (collection != null && collection > 0) {
//            predicate = ExpressionUtils.and(predicate, material.collection.eq(collection));
//        }
        Integer type = query.get("type", Integer.class);
        String orderProperty = "createdAt";
        Integer order = 1;
        if (type != null && type > 0) {
            ChargeMType mType = chargeMTypeRepository.findById(type).orElse(null);
            orderProperty = mType.getOrderColumn();
            order = mType.getOrder();
            predicate = ExpressionUtils.and(predicate, material.type.id.eq(type));
        }
        Sort.Direction direction = order == 1 ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(new Sort.Order(direction, orderProperty));
        PageRequest pageRequest = PageRequest.of(pageNo, 20, sort);
        List<ChargeMaterial> materialList = chargeMaterialRepository.findAll(predicate, pageRequest).getContent();

        return convertMaterial(materialList);
    }

    @Override
    public ChargeMaterial findMaterialById(Integer id) throws ServiceException {
        return null;
    }

    @Override
    public List<ChargeMType> queryMType() throws ServiceException {
        return null;
    }

    @Override
    public ChargeMType findMTypeById(Integer id) throws ServiceException {
        return null;
    }

    private List<ChargeMaterialVO> convertMaterial(List<ChargeMaterial> materialList) {
        List<ChargeMaterialVO> materialListVO = new ArrayList<>( materialList.size() );
        for ( ChargeMaterial chargeMaterial : materialList ) {
            ChargeMaterialVO vo = new ChargeMaterialVO();
            BeanUtils.copyProperties(chargeMaterial, vo);
            vo.setType(chargeMaterial.getType().getName());
            long baseNum = chargeMaterial.getUseNum()/100;
            if (chargeMaterial.getUseNum() == 0) {
                vo.setUseNumFake(new Random().nextInt(90) + 10 + "");
            } else if (baseNum == 0) {
                long useNumFake = chargeMaterial.getUseNum()/10 == 0 ? 100 : (chargeMaterial.getUseNum()/10)*1000;
                vo.setUseNumFake(useNumFake + new Random().nextInt(999) + "");
            } else if (baseNum >= 1 && baseNum < 10) {
                DecimalFormat df = new DecimalFormat("0.0");
                vo.setUseNumFake(df.format(chargeMaterial.getUseNum()/100f) + "W");
            } else if (baseNum >= 10) {
                vo.setUseNumFake(baseNum + "W");
            }
            materialListVO.add(vo);
        }
        return materialListVO;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public synchronized Result uploadData(ChargeShowForm form) {
        if (form.getVid() == null || form.getVid() < 1) {
            return Result.FAILURE("vid is null");
        }
        ChargeMaterial material = chargeMaterialRepository.getOne(form.getVid());
        if (form.getShowNum() != null && form.getShowNum() > 0) {
            material.setShowNum(material.getShowNum() + 1);
        }
        if (form.getUseNum() != null && form.getUseNum() > 0) {
            material.setUseNum(material.getShowNum() + 1);
        }
        chargeMaterialRepository.save(material);
        return Result.SUCCESS("upload data success!");
    }
}
