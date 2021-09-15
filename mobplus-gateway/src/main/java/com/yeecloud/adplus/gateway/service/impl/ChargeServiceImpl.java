package com.yeecloud.adplus.gateway.service.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.ChargeBannerRepository;
import com.yeecloud.adplus.dal.repository.ChargeMTypeRepository;
import com.yeecloud.adplus.dal.repository.ChargeVideoRepository;
import com.yeecloud.adplus.dal.repository.ChargeWallpaperRepository;
import com.yeecloud.adplus.gateway.controller.form.ChargeShowForm;
import com.yeecloud.adplus.gateway.controller.vo.ChargeBannerVO;
import com.yeecloud.adplus.gateway.controller.vo.ChargeMTypeVO;
import com.yeecloud.adplus.gateway.controller.vo.ChargeVideoVO;
import com.yeecloud.adplus.gateway.controller.vo.ChargeWallpaperVO;
import com.yeecloud.adplus.gateway.service.ChargeService;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.common.exception.ServiceException;
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
    ChargeVideoRepository chargeVideoRepository;

    @Autowired
    ChargeWallpaperRepository chargeWallpaperRepository;

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
    public List<ChargeVideoVO> queryVideo(ChargeShowForm form) throws ServiceException {
        QChargeVideo video = QChargeVideo.chargeVideo;
        Predicate predicate = video.deleted.eq(false);

        Integer pageNo = form.getPageNo();
        if (pageNo == null || pageNo == 0) {
            pageNo = 0;
        }
        Integer type = form.getType();
        // 默认按创建时间降序排
        String orderProperty = "createdAt";
        Integer order = 1;
        if (type != null && type > 0 && type != 1) {
            ChargeMType mType = chargeMTypeRepository.findById(type).orElse(null);
            orderProperty = mType.getOrderColumn();
            order = mType.getOrder();
            predicate = ExpressionUtils.and(predicate, video.type.id.eq(type));
        }
        Sort.Direction direction = order == 1 ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(new Sort.Order(direction, orderProperty));
        PageRequest pageRequest = PageRequest.of(pageNo, 20, sort);
        List<ChargeVideo> videoList = chargeVideoRepository.findAll(predicate, pageRequest).getContent();
        return convertVideo(videoList);
    }

    private List<ChargeVideoVO> convertVideo(List<ChargeVideo> videoList) {
        videoList.forEach(ChargeVideo::fakeData);
        List<ChargeVideoVO> videoListVO = new ArrayList<>( videoList.size() );
        for ( ChargeVideo chargeVideo : videoList ) {
            ChargeVideoVO vo = new ChargeVideoVO();
            BeanUtils.copyProperties(chargeVideo, vo);
            vo.setImgUrl(chargeVideo.getVideoCover());
            vo.setThumbUrl(chargeVideo.getVideoCoverThumb());
            vo.setTypeId(chargeVideo.getType().getId());
            vo.setTypeName(chargeVideo.getType().getEnName());
            vo.setStyle(1);
            videoListVO.add(vo);
        }
        return videoListVO;
    }

    @Override
    public List<ChargeWallpaperVO> queryWallpaper(ChargeShowForm form) throws ServiceException {
        QChargeWallpaper wallpaper = QChargeWallpaper.chargeWallpaper;
        Predicate predicate = wallpaper.deleted.eq(false);

        Integer pageNo = form.getPageNo();
        if (pageNo == null || pageNo == 0) {
            pageNo = 0;
        }
        Integer type = form.getType();
        // 默认按创建时间降序排
        String orderProperty = "createdAt";
        Integer order = 1;
        if (type != null && type > 0 && type != 100) {
            ChargeMType mType = chargeMTypeRepository.findById(type).orElse(null);
            orderProperty = mType.getOrderColumn();
            order = mType.getOrder();
            predicate = ExpressionUtils.and(predicate, wallpaper.type.id.eq(type));
        }
        Sort.Direction direction = order == 1 ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(new Sort.Order(direction, orderProperty));
        PageRequest pageRequest = PageRequest.of(pageNo, 20, sort);
        List<ChargeWallpaper> wallpaperList = chargeWallpaperRepository.findAll(predicate, pageRequest).getContent();
        return convertWallpaper(wallpaperList);
    }

    private List<ChargeWallpaperVO> convertWallpaper(List<ChargeWallpaper> wallpaperList) {
        wallpaperList.forEach(ChargeWallpaper::fakeData);
        List<ChargeWallpaperVO> wallpaperListVO = new ArrayList<>( wallpaperList.size() );
        for ( ChargeWallpaper chargeWallpaper : wallpaperList ) {
            ChargeWallpaperVO vo = new ChargeWallpaperVO();
            BeanUtils.copyProperties(chargeWallpaper, vo);
            vo.setTypeId(chargeWallpaper.getType().getId());
            vo.setTypeName(chargeWallpaper.getType().getEnName());
            vo.setStyle(2);
            wallpaperListVO.add(vo);
        }
        return wallpaperListVO;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public synchronized Result uploadDataV(ChargeShowForm form) {
        if (form.getVid() == null || form.getVid() < 1) {
            return Result.FAILURE("vid is null");
        }
        ChargeVideo video = chargeVideoRepository.getOne(form.getVid());
        if (form.getShowNum() != null && form.getShowNum() > 0) {
            video.setShowNum(video.getShowNum() + 1);
        }
        if (form.getUseNum() != null && form.getUseNum() > 0) {
            video.setUseNum(video.getShowNum() + 1);
        }
        chargeVideoRepository.save(video);
        return Result.SUCCESS("update video data success!");
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public synchronized Result uploadDataWP(ChargeShowForm form) {
        if (form.getWpid() == null || form.getWpid() < 1) {
            return Result.FAILURE("wallpaper id is null");
        }
        ChargeWallpaper wallpaper = chargeWallpaperRepository.getOne(form.getWpid());
        if (form.getShowNum() != null && form.getShowNum() > 0) {
            wallpaper.setShowNum(wallpaper.getShowNum() + 1);
        }
        if (form.getUseNum() != null && form.getUseNum() > 0) {
            wallpaper.setUseNum(wallpaper.getShowNum() + 1);
        }
        chargeWallpaperRepository.save(wallpaper);
        return Result.SUCCESS("update wallpaper data success!");
    }

    @Override
    public List<ChargeMTypeVO> queryTypeList(Integer style) {
        QChargeMType type = QChargeMType.chargeMType;
        Predicate predicate = type.deleted.eq(false);
        if (style != null && style > 0) {
            predicate = ExpressionUtils.and(predicate, type.style.eq(style));
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "rankOrder"));
            List<ChargeMType> resultList =   (List<ChargeMType>)chargeMTypeRepository.findAll(predicate, sort);
            return convertMType(resultList);
        }
        return new ArrayList<>();
    }

    private List<ChargeMTypeVO> convertMType(List<ChargeMType> resultList) {
        List<ChargeMTypeVO> mTypeVOS = new ArrayList<>( resultList.size() );
        for ( ChargeMType chargeMType : resultList ) {
            ChargeMTypeVO vo = new ChargeMTypeVO();
            BeanUtils.copyProperties(chargeMType, vo);
            mTypeVOS.add(vo);
        }
        return mTypeVOS;
    }
}
