package com.example.VNVC_1.tiemchung.model;

// Enum for Service Type
public enum ServicesType {
    SINGLE, COMBO, MODIFY;

    // Hàm hỗ trợ chuyển đổi từ String sang Enum
    public static ServicesType fromString(String type) {
        for (ServicesType t : ServicesType.values()) {
            if (t.name().equalsIgnoreCase(type)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Service not found: " + type);
    }
}
