package com.keyue.dao.model;

public class BookCate {
    private Integer id;

    private String cateName;

    public BookCate(Integer id, String cateName) {
        this.id = id;
        this.cateName = cateName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName == null ? null : cateName.trim();
    }
}