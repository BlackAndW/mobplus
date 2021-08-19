package com.yeecloud.adplus.gateway.controller.form;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/23
 */
@Data
public class ScannerForm extends TranslateForm implements Serializable{

    private String imageBase64;

    private int type;
}
