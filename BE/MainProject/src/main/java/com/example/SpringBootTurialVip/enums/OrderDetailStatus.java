package com.example.SpringBootTurialVip.enums;

import lombok.Getter;
import lombok.Setter;


public enum OrderDetailStatus {
    CHUA_TIEM("Chưa tiêm chủng"),
    DA_TIEM("Đã tiêm chủng"),
    DA_LEN_LICH("Đã cập nhật ngày tiêm"),
    QUA_HAN("Lịch tiêm quá hạn");

    private String name;

    OrderDetailStatus(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
