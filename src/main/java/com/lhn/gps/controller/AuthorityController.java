package com.lhn.gps.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.lhn.gps.dto.AuthReqDto;
import com.lhn.gps.entity.GpsUserInfo;
import com.lhn.gps.service.GpsUserInfoService;
import com.lhn.gps.utils.JwtUtils;
import com.lhn.gps.utils.MD5Utils;
import com.lhn.gps.vo.UserAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/auth")
public class AuthorityController {

    @Autowired
    private GpsUserInfoService gpsUserInfoService;

    @PostMapping("/login")
    public R login(@RequestBody AuthReqDto authReqDto) throws NoSuchAlgorithmException {
        GpsUserInfo userInfo = gpsUserInfoService.lambdaQuery().eq(GpsUserInfo::getPhone, authReqDto.getPhone()).one();
        if(userInfo == null){
            throw new ApiException("用户不存在");
        }

        if(!MD5Utils.verify(authReqDto.getPassword(), userInfo.getPassword())){
            throw new ApiException("密码错误");
        }

        return R.ok(UserAuthVo.builder().
                userId(userInfo.getId()).
                phone(userInfo.getPhone()).
                userName(userInfo.getUserName()).build().setToken(JwtUtils.createJwtToken(userInfo.getUserName(),1)));
    }
}
