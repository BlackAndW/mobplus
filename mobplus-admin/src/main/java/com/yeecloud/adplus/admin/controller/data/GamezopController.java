package com.yeecloud.adplus.admin.controller.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.admin.util.OkHttpUtils;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: Leonard
 * @create: 2021/9/26
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/data/gamezop")
public class GamezopController {

    @GetMapping
    public Result getApiResult(@RequestParam Map<String, Object> params) throws IOException {

        JSONObject apiForm = new JSONObject();
        if (params.get("startDate") != null && params.get("endDate") != null) {
            apiForm.put("start_date", params.get("startDate").toString());
            apiForm.put("end_date", params.get("endDate").toString());
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            apiForm.put("start_date", simpleDateFormat.format(new Date()));
            apiForm.put("end_date", simpleDateFormat.format(new Date()));
        }
        apiForm.put("report_config", "{\"dimensions\": {\"ad_unit\": true,\"country\": true,\"date\": true},\"metrics\": {\"total_impressions\": true,\"total_average_ecpm_usd\": true,\"total_clicks\": true,\"total_average_ctr\": true,\"total_revenue_usd\": true,\"partner_revenue_usd\": true}}");
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final Request request = new Request.Builder()
                .addHeader("id", "WYe3c14jd")
                .addHeader("auth_token", "seDQGhXmJZbzM9gsCevJF4XPeqxMqjWM")
                .url("https://arnab.gamezop.com/revenue/v2/reports")
                .post(okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(apiForm)))
                .build();
        String result = OkHttpUtils.buildNoVerifyClient().newCall(request).execute().body().string();
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.get("statusCode") != null && Integer.valueOf(jsonObject.get("statusCode").toString()) != 200) {
            return Result.FAILURE("请求失败");
        }
        JSONArray resultList = jsonObject.getJSONArray("report");
        // 根据点击次数排序
        JSONArray sortedArr = new JSONArray();
        List<JSONObject> jsonValues = JSONArray.parseArray(resultList.toJSONString(), JSONObject.class);
        jsonValues.sort((o1, o2) -> {
            Integer val_1 = o1.getInteger("total_clicks");
            Integer val_2 = o2.getInteger("total_clicks");
            return -val_1.compareTo(val_2);
        });
        sortedArr = JSONArray.parseArray(jsonValues.toString());
        if (params.get("downloadFlag") != null && Boolean.valueOf(params.get("downloadFlag").toString())) {
            data2excel(sortedArr);
            return Result.SUCCESS();
        }
        int pageNo = Integer.valueOf(params.get("pageNo").toString());
        int pageSize = Integer.valueOf(params.get("pageSize").toString());
        return Result.SUCCESS(OkHttpUtils.dataPage(sortedArr, pageNo, pageSize));
    }

    private void data2excel(JSONArray resultList) throws FileNotFoundException {
        File file = new File("../GamezopData.xlsx");
        try(OutputStream os = new FileOutputStream(file);
            Workbook wb = new SXSSFWorkbook()){
            Sheet sheet = wb.createSheet();
            List<String> title = Arrays.asList("广告单元", "国家", "总展示次数", "ecpm", "总点击次数", "广告点击率", "总收入", "伙伴收入", "日期");
            for (int rowNo = 0; rowNo < resultList.size(); rowNo++) {
                Row row = sheet.createRow(rowNo);
                List<String> dataRow = new ArrayList<>();
                if (rowNo == 0) {
                    dataRow = title;
                } else {
                    dataRow = Arrays.asList(
                            resultList.getJSONObject(rowNo - 1).getString("ad_unit"),
                            resultList.getJSONObject(rowNo - 1).getString("country"),
                            resultList.getJSONObject(rowNo - 1).getString("total_impressions"),
                            resultList.getJSONObject(rowNo - 1).getString("total_average_ecpm_usd"),
                            resultList.getJSONObject(rowNo - 1).getString("total_clicks"),
                            resultList.getJSONObject(rowNo - 1).getString("total_average_ctr"),
                            resultList.getJSONObject(rowNo - 1).getString("total_revenue_usd"),
                            resultList.getJSONObject(rowNo - 1).getString("partner_revenue_usd"),
                            resultList.getJSONObject(rowNo - 1).getString("date")
                    );
                }
                for (int cellNo = 0; cellNo < 9; cellNo++) {
                    row.createCell(cellNo).setCellValue(dataRow.get(cellNo));
                }
            }
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
