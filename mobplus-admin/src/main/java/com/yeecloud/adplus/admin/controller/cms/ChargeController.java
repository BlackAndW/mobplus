package com.yeecloud.adplus.admin.controller.cms;

import com.yeecloud.adplus.admin.controller.cms.convert.ChargeConvert;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeBannerForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeMTypeForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeMaterialForm;
import com.yeecloud.adplus.admin.controller.cms.vo.ChargeBannerVO;
import com.yeecloud.adplus.admin.controller.cms.vo.ChargeMTypeVO;
import com.yeecloud.adplus.admin.controller.cms.vo.ChargeMaterialVO;
import com.yeecloud.adplus.admin.controller.cms.vo.GameVO;
import com.yeecloud.adplus.admin.service.ChargeService;
import com.yeecloud.adplus.admin.util.AWSUtil;
import com.yeecloud.adplus.dal.entity.ChargeBanner;
import com.yeecloud.adplus.dal.entity.ChargeMType;
import com.yeecloud.adplus.dal.entity.ChargeMaterial;
import com.yeecloud.adplus.dal.entity.Game;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/cms/charge")
public class ChargeController {

    @Autowired
    ChargeService chargeService;

    @Autowired
    ChargeConvert chargeConvert;

    /***
     * banner管理
     * @param params
     * @return
     * @throws Exception
     */
    @GetMapping("banner")
    @RequiresPermissions("cms:charge:query")
    public Result getBanner(@RequestParam Map<String, Object> params) throws Exception {
        Page<ChargeBanner> page = chargeService.queryBanner(new Query(params));
        PageInfo<ChargeBannerVO> result = convertBanner(page);
        return Result.SUCCESS(result);
    }

    @PostMapping("banner")
    @RequiresPermissions("cms:charge:create")
    public Result createBanner(@RequestBody @Valid ChargeBannerForm form, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        chargeService.createBanner(form);
        return Result.SUCCESS();
    }

    @PutMapping("banner/{id}")
    @RequiresPermissions("cms:charge:edit")
    public Result updateBanner(@PathVariable Integer id, @RequestBody @Valid ChargeBannerForm form, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        chargeService.updateBanner(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping("banner")
    @RequiresPermissions("cms:charge:delete")
    public Result deleteBanner(@RequestBody Integer[] ids) throws ServiceException {
        chargeService.deleteBanner(ids);
        return Result.SUCCESS();
    }

    private PageInfo<ChargeBannerVO> convertBanner(Page<ChargeBanner> result) {
        List<ChargeBannerVO> resultList = chargeConvert.convertBanner(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }

    /***
     * 素材管理
     * @param params
     * @return
     * @throws Exception
     */
    @GetMapping("material")
    @RequiresPermissions("cms:charge:query")
    public Result getMaterial(@RequestParam Map<String, Object> params) throws Exception {
        Page<ChargeMaterial> page = chargeService.queryMaterial(new Query(params));
        PageInfo<ChargeMaterialVO> result = convertMaterial(page);
        return Result.SUCCESS(result);
    }

    @PostMapping("material")
    @RequiresPermissions("cms:charge:create")
    public Result createMaterial(@RequestBody @Valid ChargeMaterialForm form, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        chargeService.createMaterial(form);
        return Result.SUCCESS();
    }

    @PutMapping("material/{id}")
    @RequiresPermissions("cms:charge:edit")
    public Result updateMaterial(@PathVariable Integer id, @RequestBody @Valid ChargeMaterialForm form, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        chargeService.updateMaterial(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping("material")
    @RequiresPermissions("cms:charge:delete")
    public Result deleteMaterial(@RequestBody Integer[] ids) throws ServiceException {
        chargeService.deleteMaterial(ids);
        return Result.SUCCESS();
    }

    @GetMapping("material/isCopy")
    @RequiresPermissions("cms:charge:create")
    public Result isCopy(@RequestParam String name) {
        boolean isCopy = chargeService.checkVideoByName(name);
        return Result.SUCCESS(isCopy ? 1 : 2);
    }

    private PageInfo<ChargeMaterialVO> convertMaterial(Page<ChargeMaterial> result) {
        List<ChargeMaterialVO> resultList = chargeConvert.convertMaterial(result.getContent());
        resultList.forEach( item -> {
            long baseNum = item.getUseNum()/100;
            if (item.getUseNum() == 0) {
                item.setUseNumFake(new Random().nextInt(90) + 10 + "");
            } else if (baseNum == 0) {
                long useNumFake = item.getUseNum()/10 == 0 ? 100 : (item.getUseNum()/10)*1000;
                item.setUseNumFake(useNumFake + new Random().nextInt(999) + "");
            } else if (baseNum >= 1 && baseNum < 10) {
                DecimalFormat df = new DecimalFormat("0.0");
                item.setUseNumFake(df.format(item.getUseNum()/100f) + "W");
            } else if (baseNum >= 10) {
                item.setUseNumFake(baseNum + "W");
            }
        });
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }

    /***
     * 素材分类管理
     * @param params
     * @return
     * @throws Exception
     */
    @GetMapping("mtype")
    @RequiresPermissions("cms:charge:query")
    public Result getType(@RequestParam Map<String, Object> params) throws Exception {
        Page<ChargeMType> page = chargeService.queryMType(new Query(params));
        PageInfo<ChargeMTypeVO> result = convertMType(page);
        return Result.SUCCESS(result);
    }

    @PostMapping("mtype")
    @RequiresPermissions("cms:charge:create")
    public Result createMType(@RequestBody @Valid ChargeMTypeForm form, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        chargeService.createMType(form);
        return Result.SUCCESS();
    }

    @PutMapping("mtype/{id}")
    @RequiresPermissions("cms:charge:edit")
    public Result updateMType(@PathVariable Integer id, @RequestBody @Valid ChargeMTypeForm form, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        chargeService.updateMType(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping("mtype")
    @RequiresPermissions("cms:charge:delete")
    public Result deleteMType(@RequestBody Integer[] ids) throws ServiceException {
        chargeService.deleteMType(ids);
        return Result.SUCCESS();
    }

//    @RequestMapping("testWeight")
//    public Result test() {
//        chargeService.updateWeight();
//        return Result.SUCCESS();
//    }

    private PageInfo<ChargeMTypeVO> convertMType(Page<ChargeMType> result) {
        List<ChargeMTypeVO> resultList = chargeConvert.convertMType(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }
}
