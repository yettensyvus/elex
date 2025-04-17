package com.yettensyvus.elex.controller;

import com.yettensyvus.elex.config.JwtProps;
import com.yettensyvus.elex.domain.User;
import com.yettensyvus.elex.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtProps jwtProps;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader Map<String, String> headers) {
        try {
            String jwtHeaderValue = headers.get(jwtProps.getHeader());
            User user = userService.findByJwtToken(jwtHeaderValue);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
