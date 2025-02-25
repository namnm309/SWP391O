package com.example.SpringBootTurialVip.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    @Schema(example = "mã được gửi qua mail")
    private String token;

    @Schema(example = "mk mới")
    private String password;
}
