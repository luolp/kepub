package com.keyue.common.enums;

public enum DiarySecrecyType {
    ONESELF(1),
    STRANGER(2),
    APPOINT(3);

    private Integer id;

    DiarySecrecyType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
