package com.keyue.dao.model;

import com.keyue.entity.RequestParams4MarkerCommit;

import java.util.ArrayList;
import java.util.List;

public class MarkedSymbol {
    private Integer id;

    private String bookNo;

    private Integer chapterNum;

    private Integer paragraphNum;

    private Integer symbolPos;

    private String symbol;

    private String belongMarkers;

    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo == null ? null : bookNo.trim();
    }

    public Integer getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(Integer chapterNum) {
        this.chapterNum = chapterNum;
    }

    public Integer getParagraphNum() {
        return paragraphNum;
    }

    public void setParagraphNum(Integer paragraphNum) {
        this.paragraphNum = paragraphNum;
    }

    public Integer getSymbolPos() {
        return symbolPos;
    }

    public void setSymbolPos(Integer symbolPos) {
        this.symbolPos = symbolPos;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol == null ? null : symbol.trim();
    }

    public String getBelongMarkers() {
        return belongMarkers;
    }

    public void setBelongMarkers(String belongMarkers) {
        this.belongMarkers = belongMarkers == null ? null : belongMarkers.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public static List<MarkedSymbol> fromParams(RequestParams4MarkerCommit params, Marker marker){
        List<MarkedSymbol> lists = new ArrayList<>();
        int begin = Math.min(params.getAnchorPos(),params.getFocusPos());
        int end = Math.max(params.getAnchorPos(),params.getFocusPos());
        for (int i = begin,j=0; i<end; i++,j++){
            MarkedSymbol markedSymbol = new MarkedSymbol();
            markedSymbol.setBookNo(params.getBookNo());
            markedSymbol.setChapterNum(params.getChapterNum());
            markedSymbol.setParagraphNum(params.getParagraphNum());
            markedSymbol.setSymbolPos(i);
            markedSymbol.setSymbol(String.valueOf(params.getComment().charAt(j)));
            markedSymbol.setType(params.getMarkerType());   //todo 插入数据库时要进行状态叠加
            markedSymbol.setBelongMarkers(marker.getMarkerId());

            lists.add(markedSymbol);
        }
        return lists;
    }
}