package com.example.SpringBootTurialVip.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

//@JsonInclude(JsonInclude.Include.NON_NULL)//Field nào null sẽ ko hiển thị lên API Respone, nhưng ko nên xài
//Form response API
//Class này chứa các field mong muốn để chuẩn hóa API
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor

public class ApiResponse <T> {


    private int code=1000;//user có thể tra đc code này báo tình trạng gì,1000 là thành công
    private String message;
    private T result;//T là 1 kiểu dữ liệu generic có thể thay đổi theo từng loại API khác nhau


}
