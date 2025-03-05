package com.example.SpringBootTurialVip.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level= AccessLevel.PRIVATE)
public class AuthenticationRequest {
    private String username;
    private String password;

//    public AuthenticationRequest() {
//    }
//
//    public AuthenticationRequest(String username, String password) {
//        this.username = username;
//        this.password = password;
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
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    // Builder class
//    public static class Builder {
//        private String username;
//        private String password;
//
//        public Builder setUsername(String username) {
//            this.username = username;
//            return this;
//        }
//
//        public Builder setPassword(String password) {
//            this.password = password;
//            return this;
//        }
//
//        public AuthenticationRequest build() {
//            return new AuthenticationRequest(username, password);
//        }
//    }


}
