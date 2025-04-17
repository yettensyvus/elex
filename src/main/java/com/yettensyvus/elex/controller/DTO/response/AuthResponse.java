package com.yettensyvus.elex.controller.DTO.response;

import com.yettensyvus.elex.domain.constants.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private USER_ROLE role;
}
