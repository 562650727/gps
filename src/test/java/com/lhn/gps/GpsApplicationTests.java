package com.lhn.gps;

import cn.hutool.json.JSONUtil;
import com.lhn.gps.entity.GpsBlob;
import com.lhn.gps.entity.GpsUserInfo;
import com.lhn.gps.service.GpsUserInfoService;
import com.lhn.gps.utils.MinioTemplate;
import io.minio.StatObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
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

    @Value("${email.from}")
    private String email;


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
     * 发邮件
     */
    @Test
    void sendMailTest(){
        List<GpsBlob> gpsBlobs1 = new ArrayList<>();
        gpsBlobs1.add(GpsBlob.builder().id(1).build());
        List<GpsBlob> gpsBlobs2 = new ArrayList<>();
        gpsBlobs2.addAll(gpsBlobs1);
        gpsBlobs2.get(0).setId(2);
        System.out.println(gpsBlobs2.get(0));
        System.out.println(gpsBlobs1.get(0));
    }

    /**
     * 用户信息
     */
    @Test
    void getUserInfo(){
        System.out.println(email);
        List<GpsUserInfo> list = gpsUserInfoService.lambdaQuery().list();
        System.out.println(list);
    }
}
