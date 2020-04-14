package com.keyue.dao.model;

public class BookBooklists {
    private Integer listsId;

    private String bookNo;

    public Integer getListsId() {
        return listsId;
    }

    public void setListsId(Integer listsId) {
        this.listsId = listsId;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo == null ? null : bookNo.trim();
    }
}