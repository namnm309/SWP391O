package com.example.SpringBootTurialVip.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildUpdateRequest {
    private Long userId; // Dùng userId thay vì childId

    @JsonIgnore // Ẩn parentId vì nó sẽ được lấy từ user đang đăng nhập
    private Long parentid;

    private String fullname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date bod;

    private String gender;
    private double height;
    private double weight;
}
