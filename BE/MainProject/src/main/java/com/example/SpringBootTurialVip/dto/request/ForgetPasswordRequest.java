package com.example.SpringBootTurialVip.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class ForgetPasswordRequest {
    private String email;
}
