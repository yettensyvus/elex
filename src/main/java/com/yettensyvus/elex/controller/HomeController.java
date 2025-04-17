package com.yettensyvus.elex.controller;

import com.yettensyvus.elex.controller.DTO.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public ResponseEntity<ApiResponse> getHome() {
        ApiResponse response = new ApiResponse();
        response.setMessage("Welcome home!");
        return ResponseEntity.ok(response);
    }
}
