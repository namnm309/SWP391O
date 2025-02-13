package com.example.SpringBootTurialVip.controller;

import com.example.SpringBootTurialVip.dto.request.AuthenticationRequest;
import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.request.VerifyTokenRequest;
import com.example.SpringBootTurialVip.dto.response.AuthenticationResponse;
import com.example.SpringBootTurialVip.dto.response.VerifyTokenResponse;
import com.example.SpringBootTurialVip.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

//Class này verify user cung cấp password đã mã hóa có đúng ko
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor//Autowired các bean
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)//ko khai báo thì các field sẽ đc override thành private và final
public class AuthenticationController {
    //Tạo 2 dto
    //dto AuthenticationRequest để user cung cấp username và passworrd để log in
    // dto AuthenticationResponse để xem user có cung cấp đúng tk mk ko

    //Tạo AuthenticationService
    @Autowired
    AuthenticationService authenticationService;
//    AuthenticationRequest authenticationRequest;
//
//    public AuthenticationController( AuthenticationService authenticationService
//                                    ,AuthenticationRequest authenticationRequest){
//        this.authenticationService=authenticationService;
//        //this.authenticationRequest=authenticationRequest;
//    }

//    @PostMapping("/log-in")
//    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
//        boolean result = authenticationService.authencicate(authenticationRequest);
//
//
//
////        return ApiResponse.<AuthenticationResponse>builder()
////                .result(AuthenticationResponse.builder()
////                        .authenticated(request)
////                        .build())
////                .build();
//
//        return ApiResponse.<AuthenticationResponse>builder()
//                .result(new AuthenticationResponse(result))
//                .build();
//
//    }

    @PostMapping("/loginToken")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        var result = authenticationService.authencicate(authenticationRequest);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();

    }

    @PostMapping("/verifyToken")
    ApiResponse<VerifyTokenResponse> authenticate(@RequestBody VerifyTokenRequest request) throws ParseException, JOSEException {
        var result = authenticationService.verifyTokenResponse(request);

        return ApiResponse.<VerifyTokenResponse>builder()
                .result(result)
                .build();

    }

}
