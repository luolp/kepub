package com.keyue.dao.model;

import java.util.Date;

public class Booklists {
    private Integer id;

    private String listsName;

    private String listsDesc;

    private Integer userId;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getListsName() {
        return listsName;
    }

    public void setListsName(String listsName) {
        this.listsName = listsName == null ? null : listsName.trim();
    }

    public String getListsDesc() {
        return listsDesc;
    }

    public void setListsDesc(String listsDesc) {
        this.listsDesc = listsDesc == null ? null : listsDesc.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}