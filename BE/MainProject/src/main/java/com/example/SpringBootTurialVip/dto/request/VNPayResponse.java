package com.example.SpringBootTurialVip.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class VNPayResponse implements Serializable {
    private String status;
    private String message;
    private String URL;
}
