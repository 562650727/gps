package com.lhn.gps.controller;

import com.alibaba.fastjson2.JSON;
import com.lhn.gps.bo.DataReviceBo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 体统外部下发数据
 */
@RestController
@RequestMapping("/data")
public class DataReceiveController {

    /**
     * 接收数据
     * @param body
     * @return
     */
    @PostMapping("/receiveAllData")
    public DataReviceBo receiveAllData(@RequestBody String body){
        DataReviceBo dataReviceBo = JSON.parseObject(body, DataReviceBo.class);
        return dataReviceBo;
    }
}


