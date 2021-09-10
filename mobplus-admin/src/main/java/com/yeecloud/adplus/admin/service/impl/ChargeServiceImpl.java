package com.yeecloud.adplus.admin.service.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeBannerForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeMTypeForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeVideoForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeWallpaperForm;
import com.yeecloud.adplus.admin.service.ChargeService;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.ChargeBannerRepository;
import com.yeecloud.adplus.dal.repository.ChargeMTypeRepository;
import com.yeecloud.adplus.dal.repository.ChargeVideoRepository;
import com.yeecloud.adplus.dal.repository.ChargeWallpaperRepository;
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
    ChargeVideoRepository chargeVideoRepository;

    @Autowired
    ChargeMTypeRepository chargeMTypeRepository;

    @Autowired
    ChargeWallpaperRepository chargeWallpaperRepository;

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
    public Page<ChargeVideo> queryVideo(Query query) throws ServiceException {
        QChargeVideo chargeVideo = QChargeVideo.chargeVideo;
        Predicate predicate = chargeVideo.deleted.eq(false);
        Integer type;
        type = query.get("type", Integer.class);
        if (type != null && type != 1 && type > 0) {
            predicate = ExpressionUtils.and(predicate, chargeVideo.type.id.eq(type));
        }
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
        return chargeVideoRepository.findAll(predicate, pagRequest);
    }

    @Override
    public ChargeVideo findVideoById(Integer id) throws ServiceException {
        return null;
    }

    @Override
    public void createVideo(ChargeVideoForm form) throws ServiceException {
        try {
            ChargeVideo video = new ChargeVideo();
            BeanUtils.copyProperties(form, video);
            video.setVideoPath(rootPath + videoKeyPath + "material/" + form.getVideoName());
            video.setVideoIntroduce(rootPath + videoKeyPath + "material/" + form.getVideoIntroduceName());
            chargeVideoRepository.save(video);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateVideo(Integer id, ChargeVideoForm form) throws ServiceException {
        try {
            ChargeVideo video = chargeVideoRepository.findById(id).orElse(null);
            if (video != null && !video.isDeleted()) {
                BeanUtils.copyProperties(form, video);
                video.setVideoPath(rootPath + videoKeyPath + "material/" + form.getVideoName());
                video.setVideoIntroduce(rootPath + videoKeyPath + "material/" + form.getVideoIntroduceName());
                chargeVideoRepository.save(video);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteVideo(Integer[] ids) throws ServiceException {
        chargeVideoRepository.deleteById(ids);
    }

    /***
     * 壁纸CURD
     * @param query
     * @return
     * @throws ServiceException
     */
    @Override
    public Page<ChargeWallpaper> queryWallpaper(Query query) throws ServiceException {
        QChargeWallpaper chargeWallpaper = QChargeWallpaper.chargeWallpaper;
        Predicate predicate = chargeWallpaper.deleted.eq(false);
        Integer type;
        type = query.get("type", Integer.class);
        if (type != null && type != 100 && type > 0) {
            predicate = ExpressionUtils.and(predicate, chargeWallpaper.type.id.eq(type));
        }
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
        return chargeWallpaperRepository.findAll(predicate, pagRequest);
    }

    @Override
    public ChargeWallpaper findWallpaperById(Integer id) throws ServiceException {
        return null;
    }

    @Override
    public void createWallpaper(ChargeWallpaperForm form) throws ServiceException {
        try {
            ChargeWallpaper wallpaper = new ChargeWallpaper();
            BeanUtils.copyProperties(form, wallpaper);
            chargeWallpaperRepository.save(wallpaper);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateWallpaper(Integer id, ChargeWallpaperForm form) throws ServiceException {
        try {
            ChargeWallpaper wallpaper = chargeWallpaperRepository.findById(id).orElse(null);
            if (wallpaper != null && !wallpaper.isDeleted()) {
                BeanUtils.copyProperties(form, wallpaper);
                chargeWallpaperRepository.save(wallpaper);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteWallpaper(Integer[] ids) throws ServiceException {
        chargeWallpaperRepository.deleteById(ids);
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
        QChargeVideo chargeVideo = QChargeVideo.chargeVideo;
        Predicate predicate = chargeVideo.deleted.eq(false);
        List<ChargeVideo> list = (List<ChargeVideo>)chargeVideoRepository.findAll(predicate);
        list.forEach( item -> {
            item.updateWeight();
            chargeVideoRepository.save(item);
        });
    }

    @Override
    public boolean checkVideoByName(String name) {
        QChargeVideo video = QChargeVideo.chargeVideo;
        Predicate predicate = video.deleted.eq(false);
        if (name != null && name.length() > 0) {
            predicate = ExpressionUtils.and(predicate, video.videoName.eq(name));
            predicate = ExpressionUtils.or(predicate, video.videoIntroduceName.eq(name));
        }
        List<ChargeVideo> result = (List<ChargeVideo>) chargeVideoRepository.findAll(predicate);
        return result.size() > 0;
    }
}
