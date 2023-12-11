package com.lhn.gps.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Builder;
import lombok.Data;

import java.util.Date;


@Builder
@Data
public class GpsSendEmailLog extends Model<GpsSendEmailLog> {
    private Long id;
    private String email_to;
    private String subject;
    private String context;
    private String bussiness;
    private int times;
    private String state;
    private Date createTime;
}