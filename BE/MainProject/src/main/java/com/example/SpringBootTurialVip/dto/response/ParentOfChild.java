package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.enums.RelativeType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ParentOfChild {
    private Long id;
    private String fullname;
    private String email;
    private String phone;
    private String username;
    private LocalDate bod;
    private String gender;
    private boolean enabled;
    private RelativeType relativeType;

    private List<String> underlyingConditions;  // Danh sách bệnh nền của trẻ
    private List<VaccinationHistoryResponse> vaccinationHistory;  // Lịch sử tiêm chủng của trẻ
    private List<ReactionSummaryResponse> reactionHistory;  // Lịch sử phản ứng sau tiêm của trẻ



}
