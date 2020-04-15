package com.keyue.service.impl;

import com.keyue.dao.MarkedSymbolMapper;
import com.keyue.dao.MarkerMapper;
import com.keyue.dao.model.MarkedSymbol;
import com.keyue.dao.model.Marker;
import com.keyue.entity.RequestParams4MarkerCommit;
import com.keyue.entity.ResultModel;
import com.keyue.service.IMarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarkerServiceImpl implements IMarkerService {

    @Autowired
    MarkerMapper markerMapper;

    @Autowired
    MarkedSymbolMapper markedSymbolMapper;

    @Override
    public ResultModel<String> commitMarker(RequestParams4MarkerCommit params) {
        Marker marker = Marker.fromParams(params);
        List<MarkedSymbol> markedSymbols = MarkedSymbol.fromParams(params,marker);

        markerMapper.insert(marker);
        markedSymbols.forEach(item->{
            markedSymbolMapper.insert(item);
        });

        return new ResultModel<>();
    }
}
