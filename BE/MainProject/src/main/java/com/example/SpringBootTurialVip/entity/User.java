package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity//Đánh dấu đây là 1 table để map lên từ database
@Table(name="tbl_users")
// hibernate dưới jpa sẽ giúp chúng ta lm vc vs database
public class User  {
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

    @Column(name="password")
    private String password;

    @Column(name="email")
    private String email;

    @Column(name="phone")
    private String phone;

    @Column(name="birth_date")
    private Date bod;

    @Column(name="gender")
    private String gender;

        @Column(name="height")
        private Double height=0.0;

        @Column(name="weight")
        private Double weight=0.0;

    @Column(name="enabled")
    private boolean enabled;

    @Column(name="verification_cod")
    private String verificationcode;

    @Column(name="verification_expiration")
    private LocalDateTime verficationexpiration;

    @ManyToMany
    Set<Role> roles;

    private Boolean accountNonLocked;

    private String resetToken;

    //avatar
    @Column(name="avatar_url")
    private String avatarUrl;


}
