package com.example.SpringBootTurialVip.dto.request;

import lombok.Data;

@Data
public class ChildDoseRequest {
    private Long childId;
    private int dosesAlreadyTaken; // để override nếu muốn, hoặc có thể không dùng
}
