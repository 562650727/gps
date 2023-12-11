package com.lhn.gps.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class GpsBlob extends Model<GpsFile> {
    private long id;
    private String title;
    private String coverUrl;
    private String contextUrl;
    private Date createTime;
}
