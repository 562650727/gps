package com.lhn.gps.service.impl;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhn.gps.config.MinioConfig;
import com.lhn.gps.dao.GpsUserInfoDao;
import com.lhn.gps.entity.GpsUserInfo;
import com.lhn.gps.service.GpsUserInfoService;
import com.lhn.gps.utils.MinioTemplate;
import org.jodconverter.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;

@Service("gpsUserInfoService")
public class GpsUserInfoServiceImpl extends ServiceImpl<GpsUserInfoDao, GpsUserInfo> implements GpsUserInfoService {

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioTemplate minioTemplate;

    @Override
    public String uploadPhoto(MultipartFile file, String userId) throws Exception {
        if(StringUtils.isEmpty(userId)){
            throw new ApiException("userId is not null");
        }
        GpsUserInfo userInfo = getById(userId);
        if(userInfo == null){
            throw new ApiException("用户不存在");
        }
        String bucketName = minioConfig.getBucketName();
        String originalFilename = file.getOriginalFilename();
        String fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = URLEncoder.encode(fileName) + fileType;
        minioTemplate.putObject(bucketName, objectName, file);
        userInfo.setPhoto(objectName);
        saveOrUpdate(userInfo);
        return objectName;
    }
}
