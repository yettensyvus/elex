package com.yettensyvus.elex.service;

import com.yettensyvus.elex.controller.DTO.request.LoginRequest;
import com.yettensyvus.elex.controller.DTO.request.SignupRequest;
import com.yettensyvus.elex.controller.DTO.response.AuthResponse;
import com.yettensyvus.elex.domain.constants.USER_ROLE;

public interface AuthService {
    String createUser(SignupRequest request) throws Exception;

    void sendLoginOtp(String email, USER_ROLE role) throws Exception;

    AuthResponse signIn(LoginRequest request) throws Exception;
}
