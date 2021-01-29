package com.yeecloud.adplus.gateway.controller.vo;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/1/29
 */

@Data
public class AppFunctionVO implements Serializable {

    private Integer id;

    private String name;

    private List adTypeList;
}
