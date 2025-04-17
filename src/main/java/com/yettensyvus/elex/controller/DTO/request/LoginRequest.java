package com.yettensyvus.elex.controller.DTO.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String otp;
}
