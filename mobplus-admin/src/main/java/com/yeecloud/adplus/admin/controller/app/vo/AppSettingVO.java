package com.yeecloud.adplus.admin.controller.app.vo;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/8/24
 */
@Data
public class AppSettingVO {

    private static final long serialVersionUID = 1L;

    private JSONArray serverNMList = new JSONArray();
    private JSONArray serverVIPList = new JSONArray();
    private JSONArray serverBKList = new JSONArray();

//    private List<String> serverNMCheckList;
//    private List<String> serverVIPCheckList;
//    private List<String> serverBKCheckList;


}
