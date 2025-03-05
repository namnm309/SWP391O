package com.example.SpringBootTurialVip.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyAccountRequest {
    private String email;
    private String verificationCode;
}
