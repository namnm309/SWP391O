package com.example.SpringBootTurialVip.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRelativeResponse {
    private Long id;
    private String fullname;
    private LocalDate bod;
    private String gender;
}
