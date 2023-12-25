package com.lhn.gps.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.lhn.gps.entity.GpsUserInfo;
import com.lhn.gps.service.GpsUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/gps-user-info")
public class GpsUserInfoController {

    @Autowired
    private GpsUserInfoService gpsUserInfoService;

    /**
     * 获取所有用户
     * @return
     */
    @GetMapping("/getAll")
    public R<List<GpsUserInfo>> getAll(){
        List<GpsUserInfo> list = gpsUserInfoService.lambdaQuery().list();
        return R.ok(list);
    }

    /**qq
     * 上传头像
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadPhoto")
    public R uploadPhoto(@RequestParam(value = "file") MultipartFile file,
                    @RequestParam(value = "userId") String userId) throws Exception {
        return R.ok(gpsUserInfoService.uploadPhoto(file,userId));
    }

    //merge


}
