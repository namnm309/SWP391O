package com.example.SpringBootTurialVip.dto.request;

import java.time.LocalDate;

public record ConsultRequestDTO(
        String parentName,      // Tên phụ huynh
        String phone,           // Số điện thoại của phụ huynh
        String email,           // Email của phụ huynh
        String childName,       // Tên của trẻ
        LocalDate childDob,     // Ngày sinh của trẻ
        String note             // Ghi chú (nếu có)
) {}
