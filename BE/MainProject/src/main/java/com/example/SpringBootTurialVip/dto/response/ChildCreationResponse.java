package com.example.SpringBootTurialVip.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildCreationResponse {
    @NotNull
    private Long parentid;

    private String username;

    private String fullname;

    private Date bod;

    private String gender;

    private double height;

    private double weight;
}
