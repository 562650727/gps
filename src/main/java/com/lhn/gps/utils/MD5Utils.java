package com.lhn.gps.utils;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * MD5加密
 */
public class MD5Utils {

    public static String encryMD5Salt(String data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        //每次的盐都是随机的
        String salt = UUID.randomUUID().toString().substring(0, 16);
        md5.update((data + salt).getBytes());
        //获取十六进制字符串形式的MD5摘要
        String password = new String(new Hex().encode(md5.digest()));
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    /**
     * 校验密码是否正确
     */
    public static boolean verify(String password, String md5) throws NoSuchAlgorithmException {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String salt = new String(cs2);
        MessageDigest md5Verify = MessageDigest.getInstance("MD5");
        md5Verify.update((password + salt).getBytes());
        return new String(new Hex().encode(md5Verify.digest())).equals(new String(cs1));
    }
}
