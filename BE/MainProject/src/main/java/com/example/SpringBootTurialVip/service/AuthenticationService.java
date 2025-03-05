package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.dto.request.AuthenticationRequest;
import com.example.SpringBootTurialVip.dto.response.AuthenticationResponse;

public interface AuthenticationService {

    public AuthenticationResponse authencicate(AuthenticationRequest request);
}
