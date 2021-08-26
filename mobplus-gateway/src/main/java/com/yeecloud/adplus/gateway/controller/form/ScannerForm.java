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

    private String imageBase64;

    private int type;

}
