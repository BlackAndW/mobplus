package com.yeecloud.adplus.admin.controller.app;

import com.yeecloud.adplus.admin.controller.app.convert.AppLinkConvert;
import com.yeecloud.adplus.admin.controller.app.form.AppLinkForm;
import com.yeecloud.adplus.admin.controller.app.vo.AppLinkVO;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeBannerForm;
import com.yeecloud.adplus.admin.controller.cms.vo.ChargeBannerVO;
import com.yeecloud.adplus.admin.service.AppLinkService;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.AppLink;
import com.yeecloud.adplus.dal.entity.AppLinkLog;
import com.yeecloud.adplus.dal.entity.ChargeBanner;
import com.yeecloud.adplus.dal.repository.AppLinkRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2021/7/16
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/app/link")
public class AppLinkController {

    @Resource
    AppLinkRepository appLinkRepository;

    @Autowired
    AppLinkService appLinkService;

    @Autowired
    AppLinkConvert appLinkConvert;

    @GetMapping
    @RequiresPermissions("app:link:query")
    public Result getLink(@RequestParam Map<String, Object> params) throws Exception {
        Page<AppLinkLog> page = appLinkService.queryLog(new Query(params));
        PageInfo<AppLinkVO> result = convertAppLink(page);
        return Result.SUCCESS(result);
    }

    @PostMapping
    @RequiresPermissions("app:link:create")
    public Result create(@RequestBody @Valid AppLinkForm form, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appLinkService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("app:link:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid AppLinkForm form, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appLinkService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("app:link:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        appLinkService.delete(ids);
        return Result.SUCCESS();
    }

//    @RequestMapping("import")
//    public void importUrl() throws IOException {
//        FileInputStream fis = null;
//        Workbook workbook = null;
//        fis = new FileInputStream("D:\\1-project\\1.MobPlus-backend\\git-repo\\mobplus\\mobplus-admin\\src\\main\\resources\\JerryWo.xlsx");
//        workbook = new XSSFWorkbook(fis);
//        try {
//            Sheet sheet = workbook.getSheetAt(0);
//            Row rowHead = sheet.getRow(0);
//            int totalRowNum = sheet.getLastRowNum();
//            for(int i = 1 ; i <= totalRowNum ; i++)
//            {
//                //获得第i行对象
//                Row row = sheet.getRow(i);
//
//                //获得获得第i行第0列的 String类型对象
//                String name = row.getCell(0).getStringCellValue();
//                String imgUrl = row.getCell(1).getStringCellValue();
//                String imgPath = row.getCell(2).getStringCellValue();
//
//                AppLink appLink = new AppLink();
//                appLink.setName(name);
//                App app = new App();
//                app.setId(10);
//                appLink.setApp(app);
//                appLink.setImgUrl(imgUrl);
//                appLink.setImgPath(imgPath);
//                appLinkRepository.save(appLink);
//            }
//        } finally {
//            fis.close();
//            workbook.close();
//        }
//    }

    private PageInfo<AppLinkVO> convertAppLink(Page<AppLinkLog> result) {
        List<AppLinkVO> resultList = appLinkConvert.convert2(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }
}
