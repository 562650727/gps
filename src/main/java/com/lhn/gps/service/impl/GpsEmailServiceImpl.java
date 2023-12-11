package com.lhn.gps.service.impl;

import cn.hutool.extra.mail.MailUtil;
import cn.hutool.json.JSONUtil;
import com.lhn.gps.entity.GpsSendEmailLog;
import com.lhn.gps.enums.Bussiness;
import com.lhn.gps.enums.EmailLogState;
import com.lhn.gps.service.GpsEmailService;
import com.lhn.gps.service.GpsSendEmailLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class GpsEmailServiceImpl implements GpsEmailService {

    @Autowired
    private GpsSendEmailLogService gpsSendEmailLogService;

    @Override
    public void send(List<String> notifications, String bussiness,String subject,String context,Long oldSendId) {
        String state = EmailLogState.FAIL.getCode();
        try{
            MailUtil.send(notifications, subject, context, false, null);
            state = EmailLogState.SUCCESS.getCode();
        }catch (Exception e){
            log.error("邮件发送发送失败，notifications:{},bussiness:{},subject:{},context:{}", JSONUtil.toJsonStr(notifications),bussiness,subject,context);
        }
        GpsSendEmailLog gpsSendEmailLog = gpsSendEmailLogService.getById(oldSendId);
        gpsSendEmailLogService.saveOrUpdate(GpsSendEmailLog.builder().
                id(gpsSendEmailLog == null ? null : gpsSendEmailLog.getId()).
                subject(subject).
                context(context).
                bussiness(Bussiness.FILE_UPLOAD.getCode()).
                times(gpsSendEmailLog == null ? 1 : gpsSendEmailLog.getTimes()+1).
                state(state).
                createTime(new Date()).
                email_to(String.join(",", notifications)).build());
    }
}
