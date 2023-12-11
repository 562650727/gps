package com.lhn.gps.job;

import com.lhn.gps.entity.GpsSendEmailLog;
import com.lhn.gps.enums.EmailLogState;
import com.lhn.gps.service.GpsEmailService;
import com.lhn.gps.service.GpsSendEmailLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling //开启定时任
@Slf4j
public class GpsEmailJob {
    @Autowired
    private GpsEmailService gpsEmailService;
    @Autowired
    private GpsSendEmailLogService gpsSendEmailLogService;
    private final static int maxTimes = 5; //最大重试发送次数
    private static ExecutorService executor = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue(10));
    //未发送成功的日志进行补偿
    @Scheduled(cron = "0 0/1 * * * ?")
    public void retrySendEmail() {
        List<GpsSendEmailLog> list = gpsSendEmailLogService.lambdaQuery().eq(GpsSendEmailLog::getState, EmailLogState.FAIL.getCode()).lt(GpsSendEmailLog::getTimes, maxTimes).list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (GpsSendEmailLog gpsSendEmailLog : list) {
            executor.execute(() -> {
                String bussiness = gpsSendEmailLog.getBussiness();
                String subject = gpsSendEmailLog.getSubject();
                String context = gpsSendEmailLog.getContext();
                String emailTo = gpsSendEmailLog.getEmail_to();
                log.warn("邮件重试发送job ,接收人:{},主题:{},内容:{}", emailTo,subject,context);
                gpsEmailService.send(Collections.singletonList(emailTo), bussiness, subject, context,gpsSendEmailLog.getId());
            });
        }
    }
}
