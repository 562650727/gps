package com.lhn.gps.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lhn.gps.entity.GpsBlob;

public interface GpsBlobService extends IService<GpsBlob> {
    void syncBlob(String url);
}
