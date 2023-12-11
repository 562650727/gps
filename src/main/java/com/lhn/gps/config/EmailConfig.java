package com.lhn.gps.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "email")
public class EmailConfig {
    private String from; //发送人
    private List<String> notifications;//业务发送接受通知的人
}
