package com.yeecloud.adplus.gateway.controller.vo;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Huang
 * @create: 2020-12-09 14:24
 */
@Data
public class AppPosCfgVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JSONField(name = "position")
    private List<AdPositionVO> positionList = new ArrayList<>();

    @JSONField(name = "cfg")
    private Map<String, Object> params;

    private int orderConfig;
}
