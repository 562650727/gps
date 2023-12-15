package com.lhn.gps.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lhn.gps.entity.GpsUserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface GpsUserInfoService extends IService<GpsUserInfo> {

    String uploadPhoto(MultipartFile file, String userId) throws Exception;
}
