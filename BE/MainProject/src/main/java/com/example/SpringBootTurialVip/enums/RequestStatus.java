package com.example.SpringBootTurialVip.enums;

public enum RequestStatus {
    NEW("Mới tạo"),
    DONE("Đã xử lý"),
    CANCELLED("Đã hủy");

    private final String label;

    RequestStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
