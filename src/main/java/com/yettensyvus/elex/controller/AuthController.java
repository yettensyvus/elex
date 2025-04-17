package com.yettensyvus.elex.controller;

import com.yettensyvus.elex.controller.DTO.request.LoginOtpRequest;
import com.yettensyvus.elex.controller.DTO.request.LoginRequest;
import com.yettensyvus.elex.controller.DTO.request.SignupRequest;
import com.yettensyvus.elex.controller.DTO.response.ApiResponse;
import com.yettensyvus.elex.controller.DTO.response.AuthResponse;
import com.yettensyvus.elex.domain.constants.USER_ROLE;
import com.yettensyvus.elex.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(
            @RequestBody SignupRequest req
    ) throws Exception {
        String jwt = authService.createUser(req);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("Register success");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(
            @RequestBody LoginOtpRequest req
    ) throws Exception {
        authService.sendLoginOtp(req.getEmail(), req.getRole());

        ApiResponse res = new ApiResponse();
        res.setMessage("OTP sent successfully");

        return ResponseEntity.ok(res);
    }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> loginHandler(
            @RequestBody LoginRequest req
    ) throws Exception {
        AuthResponse authResponse = authService.signIn(req);
        return ResponseEntity.ok(authResponse);
    }
}

