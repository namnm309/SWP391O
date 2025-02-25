package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level= AccessLevel.PRIVATE)
public class AuthenticationResponse {
    public boolean authenticated;//true user cung cấp đúng tk mk và ngc lại
    public String token;//tạo token ra resoonse

//    public AuthenticationResponse() {
//    }
//
//    public AuthenticationResponse(boolean authenticated
//                                  ,String token) {
//        this.authenticated = authenticated;
//        this.token=token;
//    }
//
//    public boolean isAuthenticated() {
//        return authenticated;
//    }
//
//    public void setAuthenticated(boolean authenticated) {
//        this.authenticated = authenticated;
//    }
//
//    //getter token
//    public String getToken() {
//        return token;
//    }
//
//    //setToken
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    // Builder class
//    public static class Builder {
//        private boolean authenticated;
//        private String token;
//
//        public Builder setAuthenticated(boolean authenticated) {
//            this.authenticated = authenticated;
//            return this;
//        }
//
//        //Thêm token
//        public Builder setToken(String token){
//            this.token=token;
//            return this;
//        }
//        //Phương thức build để tạo đối tượng AuthenticationResponse
//        public AuthenticationResponse build() {
//            return new AuthenticationResponse(authenticated,token);
//        }
//
//        // Phương thức build để tạo đối tượng AuthenticationResponse
////        public AuthenticationResponse build() {
////            if (authenticated == null) {
////                throw new IllegalStateException("authenticated phải được thiết lập trước khi build");
////            }
////            if (token == null) {
////                throw new IllegalStateException("token phải được thiết lập trước khi build");
////            }
////            return new AuthenticationResponse(authenticated, token);
////        }
//
//
//        public boolean isAuthenticated() {
//            return authenticated;
//        }
//
//        public String getToken() {
//            return token;
//        }
//
//    }
//
//    // Phương thức builder() trả về một đối tượng Builder
//    public static Builder builder() {
//        return new Builder();
//    }
}
