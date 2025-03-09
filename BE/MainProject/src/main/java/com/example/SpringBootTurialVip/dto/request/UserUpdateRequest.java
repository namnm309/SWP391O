package com.example.SpringBootTurialVip.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserUpdateRequest {

    //private Long parentid;
    private String fullname;
    private String password;
    private String email;
    private String phone;
    private Date bod;
    private String gender;
   // private List<String> roles;

//    public Long getParentid() {
//        return parentid;
//    }
//
//    public void setParentid(Long parentid) {
//        this.parentid = parentid;
//    }
//
//    public String getFullname() {
//        return fullname;
//    }
//
//    public void setFullname(String fullname) {
//        this.fullname = fullname;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public Date getBod() {
//        return bod;
//    }
//
//    public void setBod(Date bod) {
//        this.bod = bod;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
}
