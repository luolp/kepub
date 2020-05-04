package com.keyue.controller;

import com.keyue.entity.RequestParams4MarkerCommit;
import com.keyue.entity.ResultModel;
import com.keyue.service.IMarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/marker")
public class MarkerController {

    @Autowired
    IMarkerService markerService;

    @RequestMapping(value = "/commitMarker",method = RequestMethod.POST)
    public ResultModel<String> commitMarker(RequestParams4MarkerCommit params){
//        return markerService.commitMarker(params);
        return null;
    }

    @RequestMapping(value = "/commitMarkerV2",method = RequestMethod.POST)
    public ResultModel<String> commitMarkerV2(RequestParams4MarkerCommit params){
        try {
            return markerService.commitMarkerV2(params);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
