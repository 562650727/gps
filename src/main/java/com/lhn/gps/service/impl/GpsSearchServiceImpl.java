package com.lhn.gps.service.impl;

import com.lhn.gps.service.GpsSearchService;
import com.lhn.gps.vo.SearchVO;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GpsSearchServiceImpl implements GpsSearchService {

    @Override
    public SearchVO search(String keyword) {
        SearchVO searchVO = new SearchVO();
        searchVO.setGroupInfo(SearchVO.GroupInfo.builder().filterCondition(Collections.singletonList("fileType")).intention("找信息").build());
        searchVO.setFileInfos(Collections.singletonList(SearchVO.FileInfos.builder().build()));
        return searchVO;
    }
}
