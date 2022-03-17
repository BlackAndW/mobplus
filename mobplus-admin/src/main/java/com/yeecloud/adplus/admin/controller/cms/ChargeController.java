package com.yeecloud.adplus.admin.controller.cms;

import com.yeecloud.adplus.admin.controller.cms.convert.ChargeConvert;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeBannerForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeMTypeForm;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeMaterialForm;
import com.yeecloud.adplus.admin.controller.cms.vo.*;
import com.yeecloud.adplus.admin.service.ChargeService;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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
        boolean isCopy = chargeService.checkMaterialByName(name);
        return Result.SUCCESS(isCopy ? 1 : 2);
    }

    /**
     * 修改视频虚拟数据
     * @param result
     * @return
     */
    private PageInfo<ChargeMaterialVO> convertMaterial(Page<ChargeMaterial> result) {
        result.forEach(ChargeMaterial::fakeData);
        List<ChargeMaterialVO> resultList = chargeConvert.convertMaterial(result.getContent());
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

    private PageInfo<ChargeMTypeVO> convertMType(Page<ChargeMType> result) {
        List<ChargeMTypeVO> resultList = chargeConvert.convertMType(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }

    @GetMapping("app/list")
    @RequiresPermissions("cms:charge:query")
    public Result getAppList() throws ServiceException {
        List<App> appList = chargeService.queryWallpaperAppList();
        return Result.SUCCESS(appList);
    }
}
