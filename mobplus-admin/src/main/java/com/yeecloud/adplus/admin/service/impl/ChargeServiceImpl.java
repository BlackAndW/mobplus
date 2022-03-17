package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeBannerForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeMTypeForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeMaterialForm;
import com.yeecloud.adplus.admin.service.ChargeService;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.adplus.dal.repository.ChargeBannerRepository;
import com.yeecloud.adplus.dal.repository.ChargeMTypeRepository;
import com.yeecloud.adplus.dal.repository.ChargeMaterialRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
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

    @Autowired
    AppRepository appRepository;

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
        Integer appId = query.get("appId", Integer.class);
        if (appId != null && appId > 0) {
            predicate = ExpressionUtils.and(predicate, chargeBanner.app.id.eq(appId));
        }
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
            NewBeanUtils.copyProperties(banner, form, true);
            App app = appRepository.findById(form.getAppId()).orElse(null);
            if (app == null) {
                throw new ServiceException("app is not exist!");
            }
            banner.setApp(app);
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
                NewBeanUtils.copyProperties(banner, form, true);
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
        Integer appId = query.get("appId", Integer.class);
        if (appId != null && appId > 0) {
            predicate = ExpressionUtils.and(predicate, chargeMaterial.app.id.eq(appId));
        }
        Integer type;
        type = query.get("type", Integer.class);
        if (type != null && type != 1 && type != 100 && type > 0) {
            predicate = ExpressionUtils.and(predicate, chargeMaterial.type.id.eq(type));
        }
        Integer style = query.get("style", Integer.class);
        if (style != null && style > 0) {
            predicate = ExpressionUtils.and(predicate, chargeMaterial.style.eq(style));
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
    @Transactional(rollbackFor = Throwable.class)
    public void createMaterial(ChargeMaterialForm form) throws ServiceException {
        try {
            ChargeMaterial material = new ChargeMaterial();
            NewBeanUtils.copyProperties(material, form, true);
            ChargeMType mType = chargeMTypeRepository.findById(form.getType().getId()).orElse(null);
            if (mType != null) {
                if (mType.getStyle() == 1){
                    material.setVideoPath(rootPath + videoKeyPath + "material/" + form.getVideoName());
                    material.setVideoIntroduce(rootPath + videoKeyPath + "material/" + form.getVideoIntroduceName());
                }
                material.setStyle(mType.getStyle());
            }
            for (int i = 0; i < form.getAppCheckList().size(); i++ ) {
                App app = appRepository.findById(form.getAppCheckList().get(i)).orElse(null);
                if (app == null) {
                    throw new ServiceException("app is not exist!");
                }
                ChargeMType type = chargeMTypeRepository.findByAppAndNameAndStyle(app, mType.getName(), mType.getStyle());
                if (type == null) {
                    throw new ServiceException(app.getName() + " have no type of " + mType.getName());
                }
                material.setApp(app);
                material.setType(type);
                chargeMaterialRepository.save(material);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateMaterial(Integer id, ChargeMaterialForm form) throws ServiceException {
        try {
            ChargeMaterial material = chargeMaterialRepository.findById(id).orElse(null);
            if (material != null && !material.isDeleted()) {
                NewBeanUtils.copyProperties(material, form, true);
                ChargeMType mType = chargeMTypeRepository.findById(form.getType().getId()).orElse(null);
                if (mType != null && mType.getStyle() == 1) {
                    material.setVideoPath(rootPath + videoKeyPath + "material/" + form.getVideoName());
                    material.setVideoIntroduce(rootPath + videoKeyPath + "material/" + form.getVideoIntroduceName());
                }
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
        Integer appId = query.get("appId", Integer.class);
        if (appId != null && appId > 0) {
            predicate = ExpressionUtils.and(predicate, mType.app.id.eq(appId));
        }
        Integer style = query.get("style", Integer.class);
        // style为空则取全部类别
        if (style != null && style > 0) {
            predicate = ExpressionUtils.and(predicate, mType.style.eq(style));
        }
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "style"), new Sort.Order(Sort.Direction.ASC, "rankOrder"));
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
            App app = appRepository.findById(form.getAppId()).orElse(null);
            if (app == null) {
                throw new ServiceException("app is not exist!");
            }
            mType.setApp(app);
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
            item.updateWeight();
            chargeMaterialRepository.save(item);
        });
    }

    @Override
    public boolean checkMaterialByName(String name) {
        QChargeMaterial video = QChargeMaterial.chargeMaterial;
        Predicate predicate = video.deleted.eq(false);
        if (name != null && name.length() > 0) {
            predicate = ExpressionUtils.and(predicate, video.videoName.eq(name));
            predicate = ExpressionUtils.or(predicate, video.videoIntroduceName.eq(name));
        }
        List<ChargeMaterial> result = (List<ChargeMaterial>) chargeMaterialRepository.findAll(predicate);
        return result.size() > 0;
    }

    @Override
    public List<App> queryWallpaperAppList() throws ServiceException {
        QApp qApp = QApp.app;
        Predicate predicate = qApp.deleted.eq(false);
        predicate = ExpressionUtils.and(predicate, qApp.type.eq(3));
        predicate = ExpressionUtils.and(predicate, qApp.id.notIn(33, 48, 56, 57));
        List<App> appList = (List<App>) appRepository.findAll(predicate);
        if (appList.size() == 0) {
            throw new ServiceException("cannot find app of type 3");
        }
        return appList;
    }
}
