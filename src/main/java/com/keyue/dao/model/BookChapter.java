package com.keyue.dao.model;

import java.util.Date;

public class BookChapter {
    private Integer id;

    private Integer bookId;

    private Integer chapterNo;

    private String chapterName;

    private Integer chapterNo2;

    private Integer fatherNo;

    private Integer isHasContent;

    private String fileUrl;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getChapterNo() {
        return chapterNo;
    }

    public void setChapterNo(Integer chapterNo) {
        this.chapterNo = chapterNo;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName == null ? null : chapterName.trim();
    }

    public Integer getChapterNo2() {
        return chapterNo2;
    }

    public void setChapterNo2(Integer chapterNo2) {
        this.chapterNo2 = chapterNo2;
    }

    public Integer getFatherNo() {
        return fatherNo;
    }

    public void setFatherNo(Integer fatherNo) {
        this.fatherNo = fatherNo;
    }

    public Integer getIsHasContent() {
        return isHasContent;
    }

    public void setIsHasContent(Integer isHasContent) {
        this.isHasContent = isHasContent;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}