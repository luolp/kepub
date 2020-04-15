package com.keyue.service;

import com.keyue.entity.RequestParams4MarkerCommit;
import com.keyue.entity.ResultModel;

public interface IMarkerService {
    ResultModel<String> commitMarker(RequestParams4MarkerCommit params);
}
