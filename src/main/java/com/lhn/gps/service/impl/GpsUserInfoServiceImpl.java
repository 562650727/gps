package com.lhn.gps.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhn.gps.dao.GpsUserInfoDao;
import com.lhn.gps.entity.GpsUserInfo;
import com.lhn.gps.service.GpsUserInfoService;
import org.springframework.stereotype.Service;

@Service("gpsUserInfoService")
public class GpsUserInfoServiceImpl extends ServiceImpl<GpsUserInfoDao, GpsUserInfo> implements GpsUserInfoService {

}
