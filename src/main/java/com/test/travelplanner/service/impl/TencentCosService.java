package com.test.travelplanner.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.*;
import com.qcloud.cos.utils.IOUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class TencentCosService {

    private final COSClient cosClient;

    @Value("${tencent.cos.bucket-name}")  
    private String bucketName;

    @Value("${tencent.cos.region}")
    private String region;

    public TencentCosService(COSClient cosClient) {
        this.cosClient = cosClient;
    }

    // 上传文件  
    public String uploadFile(MultipartFile file) throws IOException {  
        // 将 MultipartFile 转换为临时文件  
        File tempFile = File.createTempFile("temp", null);  
        file.transferTo(tempFile);

        // 生成文件名
        String fileName = generateFileName(Objects.requireNonNull(file.getOriginalFilename()));

        // 构造上传请求  
        String key = "uploads/" + fileName; // 文件在 COS 中的路径
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, tempFile);  

        // 上传文件  
        cosClient.putObject(putObjectRequest);

        // 删除临时文件  
        tempFile.delete();

//        // 返回文件访问路径
//        return getFileUrl(fileName);

        // 返回文件的访问 URL  
        return String.format("https://%s.cos.%s.myqcloud.com/%s", bucketName, cosClient.getClientConfig().getRegion().getRegionName(), key);  
    }  

    // 列出存储桶中的文件  
    public List<String> listFiles() {  
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();  
        listObjectsRequest.setBucketName(bucketName);  
        listObjectsRequest.setPrefix("uploads/"); // 可选：指定前缀  
        listObjectsRequest.setMaxKeys(100);  

        ObjectListing objectListing = cosClient.listObjects(listObjectsRequest);
        List<COSObjectSummary> objectSummaries = objectListing.getObjectSummaries();

        List<String> fileKeys = new ArrayList<>();  
        for (COSObjectSummary summary : objectSummaries) {  
            fileKeys.add(summary.getKey());  
        }  
        return fileKeys;  
    }

    /**
     * 删除文件
     */
    public void deleteFile(String key) {
        try {
            log.info("正在删除文件...");
            log.info("file bucketName:{}", bucketName);
            cosClient.deleteObject(bucketName, key);
        } catch (Exception e) {
            log.error("文件删除失败", e);
            throw new RuntimeException("文件删除失败");
        }
    }

    /**
     * 下载文件
     */
    public void downloadFile(String fileName, HttpServletResponse response) {
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileName);
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            COSObjectInputStream cosObjectInput = cosObject.getObjectContent();

            // 设置响应头
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            // 写入响应流
            IOUtils.copy(cosObjectInput, response.getOutputStream());
            response.flushBuffer();

        } catch (Exception e) {
            log.error("文件下载失败", e);
            throw new RuntimeException("文件下载失败");
        }
    }

    /**
     * 生成文件名
     */
    private String generateFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString().replaceAll("-", "") + extension;
    }

    /**
     * 获取文件访问路径
     */
    private String getFileUrl(String fileName) {
        return String.format("https://%s.cos.%s.myqcloud.com/%s",
                bucketName, region, fileName);
    }

}