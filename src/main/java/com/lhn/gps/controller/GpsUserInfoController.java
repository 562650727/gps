package com.lhn.gps.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.lhn.gps.entity.GpsUserInfo;
import com.lhn.gps.service.GpsUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gps-user-info")
public class GpsUserInfoController {

    @Autowired
    private GpsUserInfoService gpsUserInfoService;

    @GetMapping("/getAll")
    public R<List<GpsUserInfo>> getAll(){
        List<GpsUserInfo> list = gpsUserInfoService.lambdaQuery().list();
        return R.ok(list);
    }
}
