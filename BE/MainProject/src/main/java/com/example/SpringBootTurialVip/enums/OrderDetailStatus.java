package com.example.SpringBootTurialVip.enums;


public enum String {
    CHUA_TIEM("Chưa tiêm chủng"),
    DA_TIEM("Đã tiêm chủng"),
    DA_LEN_LICH("Đã cập nhật ngày tiêm"),
    QUA_HAN("Lịch tiêm quá hạn");

    private java.lang.String name;

    String(java.lang.String name) {
        this.name=name;
    }

    public java.lang.String getName() {
        return name;
    }
}
