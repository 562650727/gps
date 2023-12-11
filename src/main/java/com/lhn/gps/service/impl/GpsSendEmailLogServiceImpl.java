package com.lhn.gps.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhn.gps.dao.GpsSendEmailLogDao;
import com.lhn.gps.entity.GpsSendEmailLog;
import com.lhn.gps.service.GpsSendEmailLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("gpsSendEmailLogService")
@Slf4j
public class GpsSendEmailLogServiceImpl extends ServiceImpl<GpsSendEmailLogDao, GpsSendEmailLog> implements GpsSendEmailLogService {

}
