package com.yeecloud.adplus.admin.controller.file;

import com.yeecloud.adplus.admin.controller.file.vo.FileVO;
import com.yeecloud.adplus.admin.service.FileService;
import com.yeecloud.adplus.admin.util.FileUtil;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Huang
 * @create: 2020-12-01 16:09
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/file")
public class FileController {


    @Value("${file.path.game.thumb}")
    private String gameThumbPath;

    @Value("${file.path.app.icon}")
    private String appIconPath;

    @Autowired
    private FileService fileService;

    /**
     * 上传游戏缩略图
     *
     * @param file
     * @return
     */
    @PostMapping("/upload/game/thumb")
    public Result<FileVO> uploadGameThumb(@RequestParam("file") MultipartFile file) {
        // 判断文件是否为图片
        String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
        if (!FileUtil.IMAGE.equals(FileUtil.getFileType(suffix))) {
            throw new ServiceException("只能上传图片");
        }
        FileVO vo = fileService.upload(file,gameThumbPath,"/game/thumb/");
        return Result.SUCCESS(vo);
    }


    /**
     * 上传应用图标
     *
     * @param file
     * @return
     */
    @PostMapping("/upload/app/icon")
    public Result<FileVO> uploadAppIcon(@RequestParam("file") MultipartFile file) {
        // 判断文件是否为图片
        String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
        if (!FileUtil.IMAGE.equals(FileUtil.getFileType(suffix))) {
            throw new ServiceException("只能上传图片");
        }
        FileVO vo = fileService.upload(file,appIconPath,"/app/icon/");
        return Result.SUCCESS(vo);
    }
}
