package com.keyue.entity;

public class RequestParams4MarkerCommit {

    private Integer markerType;

    private String bookNo;

    private Integer chapterNum;

    private Integer paragraphNum;

    private String comment;

    private Integer anchorPos;

    private Integer focusPos;

    private String selectionText;

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
        this.bookNo = bookNo;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getAnchorPos() {
        return anchorPos;
    }

    public void setAnchorPos(Integer anchorPos) {
        this.anchorPos = anchorPos;
    }

    public Integer getFocusPos() {
        return focusPos;
    }

    public void setFocusPos(Integer focusPos) {
        this.focusPos = focusPos;
    }

    public String getSelectionText() {
        return selectionText;
    }

    public void setSelectionText(String selectionText) {
        this.selectionText = selectionText;
    }
}