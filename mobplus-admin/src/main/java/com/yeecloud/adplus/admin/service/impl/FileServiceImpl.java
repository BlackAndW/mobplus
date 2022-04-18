package com.yeecloud.adplus.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.yeecloud.adplus.admin.controller.file.vo.FileVO;
import com.yeecloud.adplus.admin.service.FileService;
import com.yeecloud.adplus.admin.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author: Huang
 * @create: 2020-12-01 16:19
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Override
    public FileVO upload(MultipartFile multipartFile,String realPath,String relativePath) {
        // 检查文件大小
        FileUtil.checkSize(2, multipartFile.getSize());
        // 获取文件后缀
        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        // 获取文件类型
        String type = FileUtil.getFileType(suffix);
        File file = FileUtil.upload(multipartFile, realPath + File.separator);
        if (ObjectUtil.isNull(file)) {
            throw new ServiceException("上传失败");
        }
        try {
            return new FileVO(
                    file.getName(),
                    relativePath + file.getName(),
                    relativePath + file.getName(),
                    suffix,
                    file.getPath(),
                    type,
                    FileUtil.getSize(multipartFile.getSize())
            );
        } catch (Exception e) {
            FileUtil.del(file);
            throw e;
        }
    }

}
