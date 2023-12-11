package com.lhn.gps.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhn.gps.dao.GpsBlobDao;
import com.lhn.gps.entity.GpsBlob;
import com.lhn.gps.service.GpsBlobService;
import com.lhn.gps.utils.SeleniumUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class GpsBlobServiceImpl extends ServiceImpl<GpsBlobDao, GpsBlob> implements GpsBlobService {

    /**
     * 遍历
     *    点击进入获取详情、返回
     * @param url
     */
    @Override
    public void syncBlob(String url) {
        List<GpsBlob> blobs = new ArrayList<>();
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        List<WebElement> elements = driver.findElements(By.className("blog-list-box"));
        log.info("elements:{}", elements.size());
        int fail = 0;
        int succ = 0;
        for (int i = 0; i < elements.size(); i++) {
            WebElement webElement = elements.get(i);
            String title = webElement.findElement(By.className("blog-list-box-top")).getText();
            try {
                String coverUrl = webElement.findElement(By.className("blog-img-box")).findElement(By.tagName("img")).getAttribute("src");
                log.info("\n获取封面:{}", coverUrl);
                String text = webElement.findElement(By.className("view-time-box")).getText();
                log.info("\n获取博客发布时间:{}", SeleniumUtils.extractDate(text));
                String message = webElement.findElement(By.xpath("//*[@id=\"userSkin\"]/div[2]/div/div[2]/div[1]/div[2]/div/article["+(i+1)+"]/a")).getAttribute("href");
                log.info("\n内容:{}",message);
                blobs.add(GpsBlob.builder().createTime(DateUtils.parseDate(Objects.requireNonNull(SeleniumUtils.extractDate(text)),"yyyy.MM.dd")).title(title).coverUrl(coverUrl).contextUrl(message).build());
                succ++;
            }catch (Exception e){
                log.error("同步title:{},失败原因:{}",title,e.getMessage());
                ++fail;
            }
        }
        log.info("本次共计{}条，成功{}条,失败{}条",elements.size(),succ,fail);
        saveBatch(blobs);
    }
}
