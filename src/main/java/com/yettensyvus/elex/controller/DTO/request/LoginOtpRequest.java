package com.yettensyvus.elex.controller.DTO.request;

import com.yettensyvus.elex.domain.constants.USER_ROLE;
import lombok.Data;

@Data
public class LoginOtpRequest {
    private String email;
    private String otp;
    private USER_ROLE role;
}
