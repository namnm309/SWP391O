package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@FieldDefaults(level= AccessLevel.PRIVATE)
public class UserResponse {
    @Id//Định nghĩa cho ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Tránh bị scan ID
    @Column(name="user_id")
     private Long id;

    @Column(name="parent_id")
     private Long parentid;

    @Column(name="username")
     private String username;

    @Column(name="fullname")
     private String fullname;

    //Trong thực tế ko trả về password
//    @Column(name="password")
//     private String password;

    @Column(name="email")
     String email;

    @Column(name="phone")
    private String phone;

    @Column(name="birth_date")
    private Date bod;

    @Column(name="gender")
    private String gender;

    @Column(name="enabled")
    private boolean enabled;

    //Roles
//    private Set<String> roles;
    private Set<RoleResponse> roles;


//    public UserResponse() {
//    }
//
//    public UserResponse(Long parentid
//            , String username
//            , String fullname
//            , String password
//            , String email
//            , String phone
//            , Date bod
//            , String gender
//            , boolean enabled) {
//        this.parentid = parentid;
//        this.username = username;
//        this.fullname = fullname;
//        this.password = password;
//        this.email = email;
//        this.phone = phone;
//        this.bod = bod;
//        this.gender = gender;
//        this.enabled = enabled;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Long getParentid() {
//        return parentid;
//    }
//
//    public void setParentid(Long parentid) {
//        this.parentid = parentid;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
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
//
//    public boolean isEnabled() {
//        return enabled;
//    }
//
//    public void setEnabled(boolean enabled) {
//        this.enabled = enabled;
//    }
}
