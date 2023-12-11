package com.lhn.gps.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class GpsFile extends Model<GpsFile> {
    private Long id;
    private String type;
    private String userId;
    private String pdfUrl;
    private String orginUrl;
    private Date createTime;
}
