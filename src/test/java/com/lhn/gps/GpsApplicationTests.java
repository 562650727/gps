package com.lhn.gps;

import cn.hutool.json.JSONUtil;
import com.lhn.gps.entity.GpsUserInfo;
import com.lhn.gps.service.GpsUserInfoService;
import com.lhn.gps.utils.MD5Utils;
import com.lhn.gps.utils.MinioTemplate;
import io.minio.StatObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;

//作用：声明当前类是springboot的测试类并且获取入口类上的相关信息 SpringBootApplication是入口类类名
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpsApplication.class)
@Slf4j
class GpsApplicationTests {

    @Autowired
    private MinioTemplate minioTemplate;

    @Autowired
    private GpsUserInfoService gpsUserInfoService;


    /**
     * minio上传下载测试
     * @throws Exception
     */
    @Test
    void minioUtilsTest() throws Exception {
        String bucketName = "gps01";
        String objectName = "测试.png";
        File file = new File("D:\\壁纸\\1.png");
        log.warn("上传文件 bucketName:{} , fileName: {}",bucketName,objectName);
        MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", new FileInputStream(file));
        minioTemplate.putObject(bucketName,objectName,multipartFile);
        StatObjectResponse objectInfo = minioTemplate.getObjectInfo(bucketName, objectName);
        log.warn("查询文件:{},数据:{}",objectName, JSONUtil.toJsonStr(objectInfo));
        String url = minioTemplate.getObjectURL(bucketName, objectName,null,null);
        log.warn("下载文件地址 :{}",url);
    }


    /**
     * 用户信息
     */
    @Test
    void getUserInfo() throws Exception {
        //创建用户信息
        GpsUserInfo userInfo = GpsUserInfo.builder().
                userName("liuhainan").
                createTime(new Date()).
                phone("15210664980").
                password(MD5Utils.encryMD5Salt("123456")).build();
        gpsUserInfoService.save(userInfo);
        //查看用户信息
        List<GpsUserInfo> list = gpsUserInfoService.lambdaQuery().list();
        System.out.println(list);
    }
}
