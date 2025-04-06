package com.example.SpringBootTurialVip.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UserConditionInfoDTO {

    /* ==== thông tin bé ==== */
    private Long childId;
    private String childName;
    private Integer childAgeMonths;

    /* ==== thông tin phụ huynh ==== */
    private Long parentId;
    private String parentName;

    /* ==== danh sách bệnh nền của bé ==== */
    private List<UnderlyingConditionResponseDTO> conditions;
}
