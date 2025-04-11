package com.example.SpringBootTurialVip.dto.request;

import com.example.SpringBootTurialVip.enums.RelativeType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CustomerWithChildRequest {
    private String username;         // Tên đăng nhập của khách hàng
    private String email;            // Email của khách hàng
    private String phone;            // Số điện thoại của khách hàng
    private String fullname;         // Họ và tên khách hàng
    private LocalDate bod;           // Ngày sinh khách hàng (cha/mẹ)
    private String gender;           // Giới tính khách hàng (cha/mẹ)
//    private String childName;        // Tên trẻ
//    private LocalDate childBod;      // Ngày sinh của trẻ
//    private String childGender;      // Giới tính của trẻ
//    private RelativeType relationshipType; // Mối quan hệ giữa trẻ và cha/mẹ
//    private double childHeight;      // Chiều cao của trẻ
//    private double childWeight;      // Cân nặng của trẻ
//    private List<UnderlyingConditionRequestDTO> childConditions; // Danh sách bệnh nền của trẻ
    private List<ChildRequest> children;  // Danh sách các trẻ em của khách hàng



}



