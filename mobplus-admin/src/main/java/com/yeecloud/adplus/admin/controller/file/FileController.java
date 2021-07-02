package com.yeecloud.adplus.admin.controller.file;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.yeecloud.adplus.admin.controller.file.vo.FileVO;
import com.yeecloud.adplus.admin.service.FileService;
import com.yeecloud.adplus.admin.util.AWSUtil;
import com.yeecloud.adplus.admin.util.FileUtil;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

    @Value("${file.path.aws.rootPath}")
    private String rootPath;

    @Value("${file.path.aws.imgKeyPath}")
    private String imgKeyPath;

    @Value("${file.path.aws.videoKeyPath}")
    private String videoKeyPath;

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

    /***
     * 上传图片
     * @param file 对象文件
     * @return
     * @throws Exception
     */
    @PostMapping("/upload/aws/img/{key}")
    public Result uploadImg(@PathVariable("key") String key, @RequestParam("file") MultipartFile file) throws Exception {
        // 判断文件是否为图片
        String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
        if (!FileUtil.IMAGE.equals(FileUtil.getFileType(suffix))) {
            return Result.FAILURE("只能上传图片");
        }

        FileVO vo = upload(imgKeyPath + key, file);
        return Result.SUCCESS(vo);
    }

    /***
     * 上传视频
     * @param fileV 对象文件
     * @return
     * @throws Exception
     */
    @PostMapping("/upload/aws/video/{key}")
    public Result uploadVideo(@PathVariable("key") String key, @RequestParam("fileV") MultipartFile fileV) throws Exception {
        // 判断文件是否为视频
        String suffix = FileUtil.getExtensionName(fileV.getOriginalFilename());
        if (!FileUtil.VIDEO.equals(FileUtil.getFileType(suffix))) {
            return Result.FAILURE("只能上传视频");
        }

        FileVO vo = upload(videoKeyPath + key, fileV);
        return Result.SUCCESS(vo);
    }

    /***
     * 初始化s3客户端并上传
     * @param desPath
     * @param file
     * @return
     * @throws Exception
     */
    private FileVO upload(String desPath, MultipartFile file) throws Exception {
        AmazonS3 s3Client = AWSUtil.init_s3Client();
        String objectKey = desPath + "/" + file.getOriginalFilename();
        AWSUtil.upload(s3Client, file, objectKey);
        FileVO vo = new FileVO();
        vo.setUrl(rootPath + objectKey);
        vo.setRealName(file.getOriginalFilename());
        return vo;
    }
}
