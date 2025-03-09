package com.example.SpringBootTurialVip.dto.request;
import com.example.SpringBootTurialVip.enums.RelativeType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Schema(description = "Autoinject ko điền")
    @JsonIgnore // Ẩn parentid khỏi response JSON
    private Long parentid;


    private String fullname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date bod;

    private String gender;

    private double height;

    private double weight;

    private RelativeType relationshipType;
}
