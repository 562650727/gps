package com.lhn.gps.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhn.gps.config.EmailConfig;
import com.lhn.gps.config.MinioConfig;
import com.lhn.gps.dao.GpsFileDao;
import com.lhn.gps.entity.GpsFile;
import com.lhn.gps.enums.Bussiness;
import com.lhn.gps.service.GpsEmailService;
import com.lhn.gps.service.GpsFileService;
import com.lhn.gps.utils.MinioTemplate;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.Date;


@Slf4j
@Service("gpsFileService")
public class GpsFileServiceImpl extends ServiceImpl<GpsFileDao, GpsFile> implements GpsFileService {
    @Autowired
    private MinioConfig minioConfig;
    @Autowired
    private EmailConfig emailConfig;
    @Autowired
    private MinioTemplate minioTemplate;
    @Autowired
    private GpsEmailService gpsEmailService;

    @Override
    public String upload(MultipartFile file) {
        String bucketName = minioConfig.getBucketName();
        String originalFilename = file.getOriginalFilename();
        String type = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = DateUtil.format(new Date(), "yyyyMMdd");
        String objectName = "gps/01/" + fileName + type;
        String subject;
        String context;
        log.warn("上传文件 bucketName:{} , fileName: {}", bucketName, objectName);
        try {
            objectName = minioTemplate.putObject(bucketName, objectName, file);
            subject = objectName + "上传成功";
            context = String.format(Bussiness.FILE_UPLOAD.getEvent(), subject);
            save(GpsFile.builder().createTime(new Date()).userId("liuhainan").orginUrl(objectName).type(type).pdfUrl(objectName).build());
            log.warn("上传成功");
        } catch (Exception e) {
            subject = objectName + "上传失败";
            context = "上传失败，minio服务器异常...\n 报错信息:\n" + e.getMessage();
            log.error("上传失败，minio服务器异常...");
        }
        gpsEmailService.send(emailConfig.getNotifications(), Bussiness.FILE_UPLOAD.getCode(), subject, context, null);
        return objectName;
    }

    @Override
    public void download(String path, String type, HttpServletResponse response) throws Exception {
        String bucketName = minioConfig.getBucketName();
        GetObjectResponse res = minioTemplate.client.getObject(GetObjectArgs.builder().bucket(bucketName).object(path).build());
        ServletOutputStream outputStream = response.getOutputStream();
        BufferedOutputStream out = new BufferedOutputStream(outputStream);
        byte[] bytes;
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
            IoUtil.copy(res, byteOut);
            bytes = byteOut.toByteArray();
        }
        String fileName = URLEncoder.encode(path.substring(path.lastIndexOf("/") + 1));
        //下载
        if ("download".equals(type)) {
            response.addHeader("content-Disposition", "attachment;fileName=" + fileName + ";fileName*=UTF-8''" + fileName);
        } else {
            response.addHeader("content-Disposition", "inline;fileName=" + fileName + ";fileName*=UTF-8''" + fileName);
        }
        out.write(bytes);
        out.flush();
    }
}
