package com.keyue.dao.model;

import com.keyue.entity.RequestParams4MarkerCommit;
import com.keyue.utils.GUIDUtils;

public class Marker {
    private Integer id;

    private String markerId;

    private String creatorId;

    private Integer markerType;

    private String bookNo;

    private Integer chapterNum;

    private Integer paragraphNum;

    private Integer posStart;

    private Integer posEnd;

    private String targetSentence;

    private String comment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMarkerId() {
        return markerId;
    }

    public void setMarkerId(String markerId) {
        this.markerId = markerId == null ? null : markerId.trim();
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId == null ? null : creatorId.trim();
    }

    public Integer getMarkerType() {
        return markerType;
    }

    public void setMarkerType(Integer markerType) {
        this.markerType = markerType;
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

    public Integer getPosStart() {
        return posStart;
    }

    public void setPosStart(Integer posStart) {
        this.posStart = posStart;
    }

    public Integer getPosEnd() {
        return posEnd;
    }

    public void setPosEnd(Integer posEnd) {
        this.posEnd = posEnd;
    }

    public String getTargetSentence() {
        return targetSentence;
    }

    public void setTargetSentence(String targetSentence) {
        this.targetSentence = targetSentence == null ? null : targetSentence.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public static Marker fromParams(RequestParams4MarkerCommit params){
        Marker marker = new Marker();
        marker.setMarkerId(GUIDUtils.generateGUID());
        marker.setBookNo(params.getBookNo());
        marker.setChapterNum(params.getChapterNum());
        marker.setParagraphNum(params.getParagraphNum());
        marker.setComment(params.getComment());
        marker.setMarkerType(params.getMarkerType());
        return marker;
    }

    public static Marker fromParamsV2(RequestParams4MarkerCommit params){
        Marker marker = new Marker();
        marker.setMarkerId(GUIDUtils.generateGUID());
        marker.setBookNo(params.getBookNo());
        marker.setChapterNum(params.getChapterNum());
        marker.setComment(params.getComment());
        marker.setMarkerType(params.getMarkerType());

        int begin = Math.min(params.getAnchorPos(),params.getFocusPos());
        int end = Math.max(params.getAnchorPos(),params.getFocusPos());
        marker.setPosStart(begin);
        marker.setPosEnd(end);
        return marker;
    }
}