package com.lhn.gps;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({"com.lhn.gps.dao"})
@SpringBootApplication(scanBasePackages = {"com.*"})
public class GpsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GpsApplication.class, args);
    }
}
