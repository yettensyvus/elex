package com.yettensyvus.elex.controller;

import com.yettensyvus.elex.config.JwtProps;
import com.yettensyvus.elex.controller.DTO.response.ApiResponse;
import com.yettensyvus.elex.service.PaymentService;
import com.yettensyvus.elex.service.SellerReportService;
import com.yettensyvus.elex.service.SellerService;
import com.yettensyvus.elex.service.TransactionService;
import com.yettensyvus.elex.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.yettensyvus.elex.domain.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final TransactionService transactionService;
    private final JwtProps jwtProps;

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> handlePaymentSuccess(
            @PathVariable String paymentId,
            @RequestParam String paymentLinkId,
            @RequestHeader Map<String, String> headers

    ) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        // TO DO

        return null;
    }

}
