package com.keyue.entity;

public class VisitLogRect {

    private String recordDate;

    private Integer totalDuration;

    private String rectType;

    public VisitLogRect() {
        this.totalDuration = 0;
        this.rectType = "";
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
    }

    public String getRectType() {
        return rectType;
    }

    public void setRectType(String rectType) {
        this.rectType = rectType;
    }
}
