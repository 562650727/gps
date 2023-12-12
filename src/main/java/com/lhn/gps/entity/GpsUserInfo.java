package com.lhn.gps.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class GpsUserInfo extends Model<GpsUserInfo> {
    private Long id;
    private String phone;
    private String wechat;
    private String province;
    private String city;
    private String photo;
    private String sex;
    private String userName;
    private String password;
    private String college;
    private Date createTime;
    private Date updateTime;
}
