package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.BookChannelReository;
import com.yeecloud.adplus.dal.repository.BookLabelReository;
import com.yeecloud.adplus.dal.repository.BookSexReository;
import com.yeecloud.adplus.dal.repository.BookTypeReository;
import com.yeecloud.adplus.gateway.controller.form.TranslateForm;
import com.yeecloud.adplus.gateway.service.impl.BaiduTransApi;
import com.yeecloud.adplus.gateway.util.FileUtil;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2022/4/2
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/trans")
public class BaiduTransController {

    @Value("${baidu.translate.appId}")
    private String appId;

    @Value("${baidu.translate.securityKey}")
    private String securityKey;

    @Autowired
    BookChannelReository bookChannelReository;

    @Autowired
    BookTypeReository bookTypeReository;

    @Autowired
    BookSexReository bookSexReository;

    @Autowired
    BookLabelReository bookLabelReository;

    private static final String TMP_FILE = "/tmp";

    public static final Integer CHANNEL_1 = 1;

    @PostMapping("domain")
    public Result translateByNovel(@RequestBody TranslateForm form) {
//        BaiduTransApi baiduTransApi = new BaiduTransApi(appId, securityKey);
//        String result = baiduTransApi.getTransResult(form.getSourceString(),
//                "auto", form.getToLang(), "novel");
//        JSONObject object = JSON.parseObject(result);
//        JSONArray trans_result = object.getJSONArray("trans_result");
//        String resultStr = "";
//        if (trans_result != null && trans_result.size() > 0) {
//            resultStr = trans_result.getJSONObject(0).getString("dst");
//        }
        return Result.SUCCESS();
    }

    @PostMapping("upload")
    public Result uploadBook(@RequestParam("book") MultipartFile book,
                             @RequestParam(value = "chapters", required = false) MultipartFile chapters) throws IOException, ServiceException {
//        String suffix = FileUtil.getExtensionName(book.getOriginalFilename());
//        if (!FileUtil.TXT.equals(FileUtil.getFileType(suffix))) {
//            return Result.FAILURE("文件格式不符");
//        }
////        File jsonFile = FileUtil.toFile(book);
//        InputStream inputStream = book.getInputStream();
//        byte[] b = new byte[inputStream.available()];
//        int i = 0;
//        while ((i = inputStream.read(b)) != -1) {
//            String str = new String(b);
//            JSONObject jsonObject = JSON.parseObject(str);
//            String title = jsonObject.getString("title");
//            String author = jsonObject.getString("author");
//            String category = jsonObject.getString("category");
//            Integer isfinish = jsonObject.getInteger("isfinish");
//            String lastUpdate = jsonObject.getString("lastUpdate");
//            Integer sexCode = jsonObject.getInteger("sexCode");
//            BookData bookData = new BookData();
//            bookData.setName(title);
//            bookData.setAuthor(author);
//            bookData.setStatus(isfinish);
//            BookChannel channel = bookChannelReository.findById(CHANNEL_1).orElse(null);
//            if (channel == null) {
//                throw new ServiceException("cannot find channel by id : 1");
//            }
//            bookData.setBookChannel(channel);
//            BookType type = bookTypeReository.findByMapRule(category);
//            bookData.setTypeOrg(category);
//            bookData.setBookType(type);
//            BookLabel label = bookLabelReository.findByZhNameAndDeleted("未分类", 0);
//            bookData.setBookLabel(label);
//            BookSex sex = bookSexReository.findById(sexCode).orElse(null);
//            if (sex == null) {
//                throw new ServiceException("cannot find sex by sexCode:" + sexCode);
//            }
//            bookData.setBookSex(sex);
//            List list = (List) jsonObject.get("chapterArr");
//            System.out.println(title + author);
//            System.out.println(list);
//        }
        return Result.SUCCESS();
    }
}
