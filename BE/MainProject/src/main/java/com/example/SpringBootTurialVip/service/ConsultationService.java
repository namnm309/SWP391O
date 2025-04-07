package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.dto.request.ConsultRequestDTO;
import com.example.SpringBootTurialVip.dto.request.StaffProcessDTO;
import com.example.SpringBootTurialVip.entity.ConsultationRequest;
import com.example.SpringBootTurialVip.enums.RequestStatus;

import java.util.List;

public interface ConsultationService {

    // Xử lý yêu cầu tư vấn từ khách hàng
    void createConsultationRequest(ConsultRequestDTO consultRequestDTO);

    // Xử lý yêu cầu tư vấn từ staff (thêm bệnh nền, vaccine, tạo đơn hàng)
    void processConsultationRequest(StaffProcessDTO staffProcessDTO);

    public void saveConsultationRequest(ConsultationRequest consultationRequest);

    public List<ConsultationRequest> getAllConsultationRequests();

    public void updateRequestStatus(Long id, RequestStatus status);
}
