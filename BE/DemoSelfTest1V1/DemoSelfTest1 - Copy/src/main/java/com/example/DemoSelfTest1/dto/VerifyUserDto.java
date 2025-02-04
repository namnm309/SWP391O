package com.example.DemoSelfTest1.dto;

import lombok.Getter;
import lombok.Setter;

//@Getter
//@Setter
public class VerifyUserDto {
    private String email;
    private String verificationCode;

    public VerifyUserDto() {
    }

    public VerifyUserDto(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}