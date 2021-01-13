package com.yeecloud.adplus.admin.controller.file.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Huang
 * @create: 2020-12-01 16:17
 */
@Data
@AllArgsConstructor
public class FileVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String realName;
    private String url;
    private String suffix;
    private String path;
    private String type;
    private String size;

}
