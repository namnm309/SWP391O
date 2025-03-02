package com.example.SpringBootTurialVip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO chứa thông tin doanh thu theo tuần, tháng hoặc năm.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueResponse {
    private Double totalRevenue;
}
