package com.keyue.entity;

//http://m.liulangcat.com/yuedu.php?book=11216&chapter=2&cname=%E5%8D%97%E9%97%A8&
// bname=%E5%9C%A8%E7%BB%86%E9%9B%A8%E4%B8%AD%E5%91%BC%E5%96%8A&dir=xddwx/07&cnum=15
public class RequestParams4Reader {
    private String bookNo;
    private String bookName;
    private Integer chapterNum;
    private Integer chapterCount;
    private String chapterName;
    private String bookFilePath;

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(Integer chapterNum) {
        this.chapterNum = chapterNum;
    }

    public Integer getChapterCount() {
        return chapterCount;
    }

    public void setChapterCount(Integer chapterCount) {
        this.chapterCount = chapterCount;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getBookFilePath() {
        return bookFilePath;
    }

    public void setBookFilePath(String bookFilePath) {
        this.bookFilePath = bookFilePath;
    }
}
