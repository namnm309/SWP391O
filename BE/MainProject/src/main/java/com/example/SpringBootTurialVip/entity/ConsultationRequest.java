package com.example.SpringBootTurialVip.entity;

import com.example.SpringBootTurialVip.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_consult_requests")  // Tên bảng trong cơ sở dữ liệu
@Getter
@Setter
public class ConsultationRequest {

    @Id
    @GeneratedValue
    private Long id;  // ID của yêu cầu tư vấn (auto-incremented)

    private String parentName;  // Tên phụ huynh
    private String phone;  // Số điện thoại của phụ huynh
    private String email;  // Email của phụ huynh

    private String childName;  // Tên trẻ
    private LocalDate childDob;  // Ngày sinh của trẻ

    private String note;  // Mô tả chi tiết yêu cầu tư vấn từ khách hàng
    @Enumerated(EnumType.STRING)
    private RequestStatus status=RequestStatus.NEW;  // Trạng thái của yêu cầu (NEW, DONE, CANCELLED)

    private LocalDateTime createdAt = LocalDateTime.now();  // Thời gian yêu cầu được tạo ra, mặc định là thời điểm hiện tại
}
