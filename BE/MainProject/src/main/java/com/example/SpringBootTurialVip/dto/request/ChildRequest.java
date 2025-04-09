package com.example.SpringBootTurialVip.dto.request;

import com.example.SpringBootTurialVip.enums.RelativeType;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ChildRequest {
    private String childName;           // Tên của trẻ
    private LocalDate childBod;     // Ngày sinh của trẻ
    private String childGender;         // Giới tính của trẻ
    private RelativeType relationshipType;    // Mối quan hệ với khách hàng (cha, mẹ, ông bà...)
    private Double childHeight;        // Chiều cao của trẻ
    private Double childWeight;        // Cân nặng của trẻ
    private List<UnderlyingConditionRequestDTO> childConditions;  // Các bệnh nền của trẻ
}
