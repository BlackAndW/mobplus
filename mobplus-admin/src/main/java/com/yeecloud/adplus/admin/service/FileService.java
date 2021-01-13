package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.file.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Huang
 * @create: 2020-12-01 16:19
 */
public interface FileService {

    FileVO upload(MultipartFile multipartFile,String realPath,String relativePath);
}
