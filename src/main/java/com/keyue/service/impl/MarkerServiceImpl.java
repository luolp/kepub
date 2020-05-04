package com.keyue.service.impl;

import com.keyue.dao.MarkedSymbolMapper;
import com.keyue.dao.MarkerMapper;
import com.keyue.dao.MarkerSymbolLinkMapper;
import com.keyue.dao.model.MarkedSymbol;
import com.keyue.dao.model.Marker;
import com.keyue.dao.model.MarkerSymbolLink;
import com.keyue.entity.RequestParams4MarkerCommit;
import com.keyue.entity.ResultModel;
import com.keyue.service.IMarkerService;
import org.apache.ibatis.javassist.bytecode.stackmap.BasicBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarkerServiceImpl implements IMarkerService {

    @Autowired
    MarkerMapper markerMapper;

    @Autowired
    MarkedSymbolMapper markedSymbolMapper;

    @Autowired
    MarkerSymbolLinkMapper markerSymbolLinkMapper;

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

    @Override
    public ResultModel<String> commitMarkerV2(RequestParams4MarkerCommit params) {
        Marker marker = Marker.fromParamsV2(params);
        List<MarkedSymbol> markedSymbols = MarkedSymbol.fromParamsV2(params,marker);

        markerMapper.insert(marker);
        markedSymbols.forEach(item->{
            markedSymbolMapper.insert(item);
            MarkerSymbolLink markerSymbolLink = new MarkerSymbolLink();
            markerSymbolLink.setMarkerId(marker.getMarkerId());
            markerSymbolLink.setSymbolId(item.getSymbolId());
            markerSymbolLinkMapper.insert(markerSymbolLink);
        });

        return new ResultModel<>();
    }
}
