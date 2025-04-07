package com.example.SpringBootTurialVip.dto.request;

import java.util.List;
import java.util.Map;

public record StaffProcessDTO(
        Long requestId,                      // ID của yêu cầu
        Long parentId,                       // ID phụ huynh nếu staff muốn liên kết với tài khoản phụ huynh
        Map<Long, List<Long>> childProductMap,  // Map từ childTempId -> vaccineIds
        Map<Long, List<String>> childConditions // Map từ childTempId -> danh sách bệnh nền
) {}
