package com.yeecloud.adplus.admin.controller.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.admin.service.AppService;
import com.yeecloud.adplus.admin.util.ExcelUtil;
import com.yeecloud.adplus.admin.util.FileUtil;
import com.yeecloud.adplus.admin.util.OkHttpUtils;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2021/4/28
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/data/vpn")
public class VpnLogController extends HttpServlet {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AppService appService;

    private static final String FILE_PATH = "/www/wwwroot/cdn/downloadFiles/logs/";
    private static final String DOWNLOAD_PATH = "https://res.sitepsapi.com/downloadFiles/logs/";

    @GetMapping
    @RequiresPermissions("dataManager:query")
    public Result getAccountLog(@RequestParam Map<String, Object> params) throws ServiceException, IOException {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(OkHttpUtils.VPN_URL + "/app/api/v1/vpn/list").newBuilder();
        final Request request = getRequest(httpBuilder, params);
        return Result.SUCCESS(OkHttpUtils.ResponseJSON(request));
    }

    @GetMapping("data2excel")
    @RequiresPermissions("dataManager:query")
    public Result downloadExcel(@RequestParam Map<String, Object> params, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServiceException, IOException {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(OkHttpUtils.VPN_URL + "/app/api/v1/vpn/data2excel").newBuilder();
        final Request request = getRequest(httpBuilder, params);
        String downloadFilename = OkHttpUtils.ResponseJSON(request).get("file").toString();
        log.info("download filename is: " + downloadFilename);
        return Result.SUCCESS("http://res.turbovpns.com/" + downloadFilename);
    }

    @GetMapping("errorLog")
    @RequiresPermissions("dataManager:query")
    public Result getErrorLog(@RequestParam Map<String, Object> params) throws ServiceException, IOException {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(OkHttpUtils.VPN_URL + "/app/api/v1/vpn/list/errorLog").newBuilder();
        final Request request = getRequest(httpBuilder, params);
        return Result.SUCCESS(OkHttpUtils.ResponseJSON(request));
    }

    @GetMapping("errorLog/download")
    @RequiresPermissions("dataManager:query")
    public Result downloadErrorLog(@RequestParam Map<String, Object> params) throws ServiceException, IOException {
        params.put("pageNo", 1);
        params.put("pageSize", 99999);
        HttpUrl.Builder httpBuilder = HttpUrl.parse(OkHttpUtils.VPN_URL + "/app/api/v1/vpn/list/errorLog").newBuilder();
        final Request request = getRequest(httpBuilder, params);
        JSONObject result = OkHttpUtils.ResponseJSON(request);
        JSONArray dataList = result.getJSONArray("data");
        String fileName = makeExcel(dataList);
        String downloadUrl = DOWNLOAD_PATH + fileName;
        return Result.SUCCESS(downloadUrl);
    }

    private Request getRequest(HttpUrl.Builder httpBuilder, Map<String, Object> params) throws ServiceException {
        params.put("pkgName", "");
        for (Map.Entry<String, Object> param : params.entrySet()) {
            // 根据字段id查找对应的包名
            if (param.getKey().equals("appId")) {
                App app = appService.findById(Integer.valueOf(param.getValue().toString()));
                params.replace("pkgName", app.getPkgName());
            }
            httpBuilder.addQueryParameter(param.getKey(), String.valueOf(param.getValue()));
        }
        return new Request.Builder().url(httpBuilder.build()).get().build();
    }

    private String makeExcel(JSONArray list) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        String[] cols = {"errMsg", "userIp", "country", "region","city", "serverName", "os", "pkgVersion", "createdAt"};
        ExcelUtil.writeJSONArrayToExcel(sheet, list, cols, 0, 0);
        for (int i = 0; i < cols.length; i++) {
            sheet.autoSizeColumn(i);
        }
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +"_errLog.xlsx";
        FileOutputStream out = new FileOutputStream(FILE_PATH + fileName);
        workbook.write(out);
        out.close();
        return fileName;
    }
}
