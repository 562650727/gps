package com.lhn.gps.service.impl;

import cn.hutool.core.text.UnicodeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhn.gps.config.MinioConfig;
import com.lhn.gps.dao.GpsUserInfoDao;
import com.lhn.gps.entity.GpsUserInfo;
import com.lhn.gps.service.GpsUserInfoService;
import com.lhn.gps.utils.MinioTemplate;
import io.minio.StatObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.util.Base64;

@Service("gpsUserInfoService")
public class GpsUserInfoServiceImpl extends ServiceImpl<GpsUserInfoDao, GpsUserInfo> implements GpsUserInfoService {

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioTemplate minioTemplate;

    @Override
    public String uploadPhoto(MultipartFile file, String userId) throws Exception {
        String bucketName = minioConfig.getBucketName();
        String originalFilename = file.getOriginalFilename();
        String fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = URLEncoder.encode(fileName) + fileType;
        minioTemplate.putObject(bucketName, objectName, file);
        return objectName;
    }
}
