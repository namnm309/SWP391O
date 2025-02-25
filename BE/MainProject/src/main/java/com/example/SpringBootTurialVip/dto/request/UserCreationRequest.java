package com.example.SpringBootTurialVip.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

//@Getter
//@Setter
//@Data //= @Getter + @Setter +@RequiredArgsConstructor + @ToString + @EqualsAndHashCode
//@NoArgsConstructor //( dùng kèm @Data để tạo Constructor ko tham số vì @Data chỉ có Constructor như trên )
//@AllArgsConstructor //Dùng kèm @Data
//@Builder //Công dụng bên UserService , tạo nên 1 builder class cho DTO
//@FieldDefaults(level=AccessLevel.PRIVATE) //Nếu ko khai báo thì các field sẽ auto private
public class UserCreationRequest {
    private Long parentid;

    private String username;


    private String fullname;

    @Size(min=8,message = "PASSWORD_INVALID")//Set rule nhập vào
    private String password;

    private String email;
    private String phone;
    private Date bod;
    private String gender;

    public Long getParentid() {
        return parentid;
    }

    public void setParentid(Long parentid) {
        this.parentid = parentid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBod() {
        return bod;
    }

    public void setBod(Date bod) {
        this.bod = bod;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
