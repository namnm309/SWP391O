package com.example.SpringBootTurialVip.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildCreationRequest {
    @NotNull
    @Schema(description = "Phải có")
    private Long parentid;

//    private String username;

    private String fullname;

    private Date bod;

    private String gender;

    private double height;

    private double weight;
}
