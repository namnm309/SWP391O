package com.example.SpringBootTurialVip.enums;

import lombok.Getter;
import lombok.Setter;


public enum OrderDetailStatus {
    CHUA_TIEM("Chưa tiêm chủng"),
    DA_TIEM("Đã tiêm chủng");

    private String name;

    OrderDetailStatus(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
