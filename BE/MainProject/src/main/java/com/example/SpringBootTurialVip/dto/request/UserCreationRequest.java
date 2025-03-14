package com.example.SpringBootTurialVip.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
//@Data //= @Getter + @Setter +@RequiredArgsConstructor + @ToString + @EqualsAndHashCode
//@NoArgsConstructor //( dùng kèm @Data để tạo Constructor ko tham số vì @Data chỉ có Constructor như trên )
//@AllArgsConstructor //Dùng kèm @Data
//@Builder //Công dụng bên UserService , tạo nên 1 builder class cho DTO
//@FieldDefaults(level=AccessLevel.PRIVATE) //Nếu ko khai báo thì các field sẽ auto private
public class UserCreationRequest {
    @Schema(description = "Autoinject ko điền")
    @JsonIgnore // Ẩn parentid khỏi response JSON
    private Long parentid;
    private String username;
    private String fullname;
    @Size(min=8,message = "PASSWORD_INVALID")//Set rule nhập vào
    private String password;
    private String email;
    private String phone;
    private LocalDate bod;
    private String gender;
    @JsonIgnore
    private String image;


    public UserCreationRequest(String username,
                               String fullname,
                               String password,
                               String email,
                               String phone,
                               LocalDate bod,
                               String gender) {
        this.username=username;
        this.fullname=fullname;
        this.password=password;
        this.email=email;
        this.phone=phone;
        this.bod=bod;
        this.gender=gender;

    }
}
