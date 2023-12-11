package com.lhn.gps.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.lhn.gps.service.GpsFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private GpsFileService gpsFileService;

    /**
     * 上传文件到minio
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/upload")
    public R upload(@RequestParam(value = "file") MultipartFile file) throws Exception {
        return R.ok(gpsFileService.upload(file));
    }

    /**
     * 下载文件
     * @param path
     * @param type
     * @param response
     * @throws Exception
     */
    @GetMapping("/download")
    public void download(@RequestParam(value = "path") String path,
                         @RequestParam(value = "type",required = false,defaultValue = "download") String type,
                         HttpServletResponse response) throws Exception {
        gpsFileService.download(path, type,response);
    }
}
