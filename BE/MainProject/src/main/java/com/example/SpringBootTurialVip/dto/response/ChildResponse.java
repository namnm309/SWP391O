package com.example.SpringBootTurialVip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildResponse {
    private String fullname;
    private Date bod;
    private String gender;
    private Double height;
    private Double weight;

}
