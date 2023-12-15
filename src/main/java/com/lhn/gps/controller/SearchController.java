package com.lhn.gps.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.lhn.gps.service.GpsSearchService;
import com.lhn.gps.vo.SearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private GpsSearchService gpsSearchService;

    @GetMapping
    public R<SearchVO> search(@RequestParam String keyword){
        return R.ok(gpsSearchService.search(keyword));
    }
}
