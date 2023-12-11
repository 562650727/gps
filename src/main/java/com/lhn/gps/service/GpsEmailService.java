package com.lhn.gps.service;

import com.lhn.gps.enums.Bussiness;

import java.util.List;

public interface GpsEmailService {
    //根据业务组装邮件内容 标题发送邮件
    void send(List<String> notifications, String bussiness,String subject, String context,Long oldSendId);
}
