package com.yeecloud.adplus.gateway.controller;

import com.yeecloud.adplus.gateway.controller.form.ChargeShowForm;
import com.yeecloud.adplus.gateway.controller.form.ChargeTypeForm;
import com.yeecloud.adplus.gateway.controller.vo.ChargeBannerVO;
import com.yeecloud.adplus.gateway.controller.vo.ChargeMTypeVO;
import com.yeecloud.adplus.gateway.controller.vo.ChargeMaterialVO;
import com.yeecloud.adplus.gateway.service.ChargeService;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.common.exception.ServiceException;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 充电秀
 * @author: Leonard
 * @create: 2021/7/1
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/cs")
public class ChargeShowController {

    @Autowired
    ChargeService chargeService;

    /***
     * 获取banner
     * @throws ServiceException
     */
    @PostMapping("bannerList")
    public Result getBannerList(@RequestBody ChargeShowForm form,
                                @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        return Result.isEncode(apiVersion, chargeService.queryBanner(form));
    }

    /**
     * 获取类型列表
     * @param style 1:视频(默认);2:壁纸
     * @param toLang 语言简称,例如:zh, en
     */
    @GetMapping("type")
    public Result getType(@RequestParam(defaultValue = "1") Integer style,
                          @RequestParam(required = false) String toLang,
                          @RequestParam(defaultValue = "61c43dcde4b02a19c9ef5c26") String appId,
                          @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        List<ChargeMTypeVO> result = chargeService.queryTypeList(style, toLang, appId);
        return Result.isEncode(apiVersion, result);
    }

    /**
     * 获取类型列表-新接口
     * @param form-style 1:视频(默认);2:壁纸
     * @param form-toLang 语言简称,例如:zh, en
     * @param form-appId
     */
    @PostMapping("type/new")
    public Result getTypeNew(@RequestBody ChargeTypeForm form,
                          @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        List<ChargeMTypeVO> result = chargeService.queryTypeListNew(form);
        return Result.isEncode(apiVersion, result);
    }

    /**
     * 获取素材列表
     * @param form-pageNo 【body参数】页码 起始页为0
     * @param form-type 【body参数】类型id ('全部'类型的typeId为1, 壁纸的为100)
     * @param form-style 【body参数】素材类型 1:视频(默认), 2:壁纸
     * @return
     * @throws ServiceException
     */
    @PostMapping("materialList")
    public Result getMaterialList(@RequestBody ChargeShowForm form,
                                  @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        return Result.isEncode(apiVersion, chargeService.queryMaterial(form));
    }

    /**
     * 更新素材数据
     * @param form-id 【body参数】素材id
     * @param form-showNum 【body参数】展示次数 传0或1
     * @param form-useNum 【body参数】使用次数 传0或1
     * @return
     */
    @PostMapping("uploadData")
    public Result uploadData(@RequestBody ChargeShowForm form) {
        return chargeService.uploadDataV(form);
    }

}
