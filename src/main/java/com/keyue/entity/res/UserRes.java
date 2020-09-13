package com.keyue.entity.res;

import java.util.Date;

public class UserRes {
    private String keno;

    private String email;

    private String nickname;

    private String avatar;

    private Boolean isCanUpdateKeno;

    private Date createTime;

    public String getKeno() {
        return keno;
    }

    public void setKeno(String keno) {
        this.keno = keno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getCanUpdateKeno() {
        return isCanUpdateKeno;
    }

    public void setCanUpdateKeno(Boolean canUpdateKeno) {
        isCanUpdateKeno = canUpdateKeno;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
