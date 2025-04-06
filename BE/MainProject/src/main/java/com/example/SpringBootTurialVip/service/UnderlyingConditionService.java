package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.dto.request.UnderlyingConditionRequestDTO;
import com.example.SpringBootTurialVip.dto.response.ChildHealthInfoDTO;
import com.example.SpringBootTurialVip.dto.response.UnderlyingConditionResponseDTO;
import com.example.SpringBootTurialVip.dto.response.UserConditionInfoDTO;

import java.util.List;

public interface UnderlyingConditionService {

    // Thêm bệnh nền cho User
    UnderlyingConditionResponseDTO addConditionToUser(Long userId, UnderlyingConditionRequestDTO dto);

    // Cập nhật bệnh nền cho User
    UnderlyingConditionResponseDTO updateConditionForUser(Long userId, Long conditionId, UnderlyingConditionRequestDTO dto);

    // Xoá bệnh nền của User
    void removeConditionFromUser(Long userId, Long conditionId);

    // Lấy danh sách bệnh nền của User
    UserConditionInfoDTO getConditionsByUser(Long userId);

    List<UnderlyingConditionResponseDTO> getUserUnderlyingConditions(Long userId);

    ChildHealthInfoDTO getChildFullInfo(Long childId);

}
