package com.lhn.gps.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lhn.gps.entity.GpsFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface GpsFileService extends IService<GpsFile> {

    String upload(MultipartFile file) throws Exception;

    void download(String path, String type, HttpServletResponse response) throws Exception;
}
