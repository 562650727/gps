package com.lhn.gps.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtils {

    private static int ONE_DAY = 24 * 60 * 60 * 1000;

    private static String jwtKey = "sdfds&gpsqwe";

    public static String createJwtToken(String userId, int num) {
        Map<String, Object> payload = new HashMap<>();
        DateTime curr = DateTime.now();
        //签发时间
        payload.put(JWTPayload.ISSUED_AT, curr);
        //过期时间
        payload.put(JWTPayload.EXPIRES_AT, DateUtil.date(curr.getTime() + ONE_DAY * num));
        //生效时间
        payload.put(JWTPayload.NOT_BEFORE, curr);
        //载荷
        payload.put("userName", userId);
        return JWTUtil.createToken(payload, jwtKey.getBytes());
    }
}
