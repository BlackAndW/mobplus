package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeBannerForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeMTypeForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeMaterialForm;
import com.yeecloud.adplus.admin.service.ChargeService;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.ChargeBannerRepository;
import com.yeecloud.adplus.dal.repository.ChargeMTypeRepository;
import com.yeecloud.adplus.dal.repository.ChargeMaterialRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.DateUtils;
import com.yeecloud.meeto.common.util.Query;
import com.yeecloud.meeto.common.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/29
 */
@Service
public class ChargeServiceImpl implements ChargeService {

    @Value("${file.path.aws.rootPath}")
    String rootPath;

    @Value("${file.path.aws.imgKeyPath}")
    String imgKeyPath;

    @Value("${file.path.aws.videoKeyPath}")
    String videoKeyPath;

    @Autowired
    ChargeBannerRepository chargeBannerRepository;

    @Autowired
    ChargeMaterialRepository chargeMaterialRepository;

    @Autowired
    ChargeMTypeRepository chargeMTypeRepository;

    /***
     * Banner CURD
     * @param query
     * @return
     * @throws ServiceException
     */
    @Override
    public Page<ChargeBanner> queryBanner(Query query) throws ServiceException {
        QChargeBanner chargeBanner = QChargeBanner.chargeBanner;
        Predicate predicate = chargeBanner.deleted.eq(false);
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "order"));
        PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
        return chargeBannerRepository.findAll(predicate, pagRequest);
    }

    @Override
    public ChargeBanner findBannerById(Integer id) throws ServiceException {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createBanner(ChargeBannerForm form) throws ServiceException {
        try {
            ChargeBanner banner = new ChargeBanner();
            BeanUtils.copyProperties(form, banner);
            chargeBannerRepository.save(banner);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateBanner(Integer id, ChargeBannerForm form) throws ServiceException {
        try {
            ChargeBanner banner = chargeBannerRepository.findById(id).orElse(null);
            if (banner != null && !banner.isDeleted()) {
                BeanUtils.copyProperties(form, banner);
                chargeBannerRepository.save(banner);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteBanner(Integer[] ids) throws ServiceException {
        chargeBannerRepository.deleteById(ids);
    }

    /***
     * 素材CURD
     * @param query
     * @return
     * @throws ServiceException
     */
    @Override
    public Page<ChargeMaterial> queryMaterial(Query query) throws ServiceException {
        QChargeMaterial chargeMaterial = QChargeMaterial.chargeMaterial;
        Predicate predicate = chargeMaterial.deleted.eq(false);
        Integer type;
        type = query.get("type", Integer.class);
        if (type != null && type == 99) {
            predicate = ExpressionUtils.and(predicate, chargeMaterial.type.id.eq(99));
        } else {
            predicate = ExpressionUtils.and(predicate, chargeMaterial.type.id.eq(1));
        }
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
        return chargeMaterialRepository.findAll(predicate, pagRequest);
    }

    @Override
    public ChargeMaterial findMaterialById(Integer id) throws ServiceException {
        return null;
    }

    @Override
    public void createMaterial(ChargeMaterialForm form) throws ServiceException {
        try {
            ChargeMaterial material = new ChargeMaterial();
            BeanUtils.copyProperties(form, material);
            material.setVideoPath(rootPath + videoKeyPath + "material/" + form.getVideoName());
            material.setVideoIntroduce(rootPath + videoKeyPath + "material/" + form.getVideoIntroduceName());
            chargeMaterialRepository.save(material);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateMaterial(Integer id, ChargeMaterialForm form) throws ServiceException {
        try {
            ChargeMaterial material = chargeMaterialRepository.findById(id).orElse(null);
            if (material != null && !material.isDeleted()) {
                BeanUtils.copyProperties(form, material);
                material.setVideoPath(rootPath + videoKeyPath + "material/" + form.getVideoName());
                material.setVideoIntroduce(rootPath + videoKeyPath + "material/" + form.getVideoIntroduceName());
                chargeMaterialRepository.save(material);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteMaterial(Integer[] ids) throws ServiceException {
        chargeMaterialRepository.deleteById(ids);
    }

    /***
     * 素材类型CURD
     * @param query
     * @return
     * @throws ServiceException
     */
    @Override
    public Page<ChargeMType> queryMType(Query query) throws ServiceException {
        QChargeMType mType = QChargeMType.chargeMType;
        Predicate predicate = mType.deleted.eq(false);
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "rankOrder"));
        PageRequest pageRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
        return chargeMTypeRepository.findAll(predicate, pageRequest);
    }

    @Override
    public ChargeMType findMTypeById(Integer id) throws ServiceException {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createMType(ChargeMTypeForm form) throws ServiceException {
        try {
            ChargeMType mType = new ChargeMType();
            BeanUtils.copyProperties(form, mType);
            chargeMTypeRepository.save(mType);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateMType(Integer id, ChargeMTypeForm form) throws ServiceException {
        try {
            ChargeMType mType = chargeMTypeRepository.findById(id).orElse(null);
            if (mType != null && !mType.isDeleted()) {
                BeanUtils.copyProperties(form, mType);
                chargeMTypeRepository.save(mType);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteMType(Integer[] ids) throws ServiceException {
        chargeMTypeRepository.deleteById(ids);
    }

    @Async
    @Override
    public void updateWeight() {
        QChargeMaterial chargeMaterial = QChargeMaterial.chargeMaterial;
        Predicate predicate = chargeMaterial.deleted.eq(false);
        List<ChargeMaterial> list = (List<ChargeMaterial>)chargeMaterialRepository.findAll(predicate);
        list.forEach( item -> {
            DecimalFormat df = new DecimalFormat("0.00");
            long useNum = item.getUseNum() == 0 ? 1 : item.getUseNum();
            long showNum = item.getShowNum() == 0 ? 1 : item.getShowNum();
            float div = Float.valueOf(df.format((float)useNum/showNum));
            // 计算和上传日期相隔的天数
            long cday = (System.currentTimeMillis() - item.getCreatedAt())/86400000;
            item.setWeight(useNum * 10 + (long)((div - cday)*1000));
            chargeMaterialRepository.save(item);
        });
    }

    @Override
    public boolean checkVideoByName(String name) {
        QChargeMaterial material = QChargeMaterial.chargeMaterial;
        Predicate predicate = material.deleted.eq(false);
        if (name != null && name.length() > 0) {
            predicate = ExpressionUtils.and(predicate, material.videoName.eq(name));
            predicate = ExpressionUtils.or(predicate, material.videoIntroduceName.eq(name));
        }
        List<ChargeMaterial> result = (List<ChargeMaterial>) chargeMaterialRepository.findAll(predicate);
        return result.size() > 0;
    }
}
