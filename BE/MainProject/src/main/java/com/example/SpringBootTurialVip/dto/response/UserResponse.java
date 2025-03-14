package com.example.SpringBootTurialVip.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor
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
    private LocalDate bod;

    @Column(name="gender")
    private String gender;

    @Column(name="avatar_url")
    private String avatarUrl;
}
