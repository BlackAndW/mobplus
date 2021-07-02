package com.yeecloud.adplus.admin.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/29
 */
@Slf4j
public class AWSUtil {

    private static final String BucketName = "charge-show-storage";

    /***
     * 初始化s3客户端并建立连接
     * @return
     * @throws Exception
     */
    public static AmazonS3 init_s3Client() throws Exception {
        Regions clientRegion = Regions.AP_SOUTHEAST_1;
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(clientRegion)
                .build();
    }

    /***
     * 获取桶对象
     * @param s3Client
     * @param bucketName
     * @return
     */
    public static Bucket getBucket(AmazonS3 s3Client, String bucketName){
        List<Bucket> buckets = s3Client.listBuckets();
        for (Bucket b : buckets) {
            if (b.getName().equals(bucketName)) {
                System.out.println(b.getName());
                return b;
            }
        }
        return null;
    }

    public static void upload(AmazonS3 s3Client, MultipartFile file, String objectKey) throws IOException {
        log.info("Uploading %s to S3 bucket %s...\n", objectKey, BucketName);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        PutObjectRequest request = new PutObjectRequest(BucketName, objectKey, file.getInputStream(), metadata);
        request.setCannedAcl(CannedAccessControlList.PublicRead);
        try {
            s3Client.putObject(request);
            log.info(objectKey + " has been uploaded!");
        } catch (AmazonServiceException e) {
            log.error(e.getErrorMessage());
        }
    }

    public static void updateObjectACL(AmazonS3 s3Client, String objectKey) {
        try {
            // get the current ACL
            AccessControlList acl = s3Client.getObjectAcl(BucketName, objectKey);
            if (acl != null) {
                // set access for the grantee
                Permission permission = Permission.Read;
                acl.grantPermission(GroupGrantee.AllUsers, permission);
                s3Client.setObjectAcl(BucketName, objectKey, acl);
            }
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
    }
}
