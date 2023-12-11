package com.lhn.gps.utils;

import com.lhn.gps.entity.GpsBlob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class SeleniumUtils {
    /**
     * Selenium 控制chrome
     */
    public static void main(String[] args){
        List<GpsBlob> blobs = new ArrayList<>();
        WebDriver driver = new ChromeDriver();
        driver.get("https://blog.csdn.net/Pan_peter?type=blog");
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
                String message = new ChromeDriver().findElement(By.className("blog-content-box")).getText();
                log.info("\n内容:{}",message);
                blobs.add(GpsBlob.builder().createTime(DateUtils.parseDate(Objects.requireNonNull(SeleniumUtils.extractDate(text)),"yyyy.MM.dd")).
                        title(title).coverUrl(coverUrl).build());
                succ++;
            }catch (Exception e){
                log.error("同步title:{},失败原因:{}",title,e.getMessage());
                ++fail;
            }
            log.info("本次共计{}条，成功{}条,失败{}条",elements.size(),succ,fail);
        }
    }

    public static String extractDate(String content) {
        Pattern pattern = Pattern.compile("((([0-9]{4}).([0-9]{2}|[1-9])).([0-9]{2}|[1-9]))"); //尝试提取这样类型的数据
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }
}
