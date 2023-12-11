package com.lhn.gps.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.lhn.gps.service.GpsEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private GpsEmailService gpsEmailService;

    /**
     * 发送邮件
     */
    @PostMapping("/send")
    public R send(String[] to, String bussiness,String subject, String context) {
        gpsEmailService.send(Arrays.asList(to), bussiness, subject, context,null);
        return R.ok("发送完成.");
    }
}
