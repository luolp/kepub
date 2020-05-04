package com.keyue.dao.model;

public class MarkerSymbolLink {
    private Integer id;

    private String markerId;

    private String symbolId;

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

    public String getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(String symbolId) {
        this.symbolId = symbolId == null ? null : symbolId.trim();
    }
}