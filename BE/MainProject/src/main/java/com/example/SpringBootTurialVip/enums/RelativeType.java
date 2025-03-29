package com.example.SpringBootTurialVip.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RelativeType {
    ANH_CHI("Anh/Chị"),
    CHU_THIEM("Chú/Thím"),
    CHA_ME("Cha/Mẹ"),
    ONG_BA("Ông/Bà");

    private final String display;

    RelativeType(String display) {
        this.display = display;
    }

    @JsonValue
    public String getDisplay() {
        return display;
    }
}
