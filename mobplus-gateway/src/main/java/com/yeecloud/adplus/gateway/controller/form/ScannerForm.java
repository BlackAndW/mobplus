package com.yeecloud.adplus.gateway.controller.form;

import lombok.Data;
import net.bytebuddy.implementation.bind.annotation.Super;

import java.io.Serializable;

/**
 * @author: Leonard
 * @create: 2021/6/23
 */
@Data
public class ScannerForm extends TranslateForm implements Serializable{

    /** base64编码后的图片 */
    private String imageBase64;

    /** 图片类型 0: 通用; 1: 动物; 2: 植物; 3: 食物; 4: 货币; 5: 地标风景 */
    private int type;

}
