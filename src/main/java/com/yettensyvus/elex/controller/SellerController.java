package com.yettensyvus.elex.controller;

import com.yettensyvus.elex.config.CorsProps;
import com.yettensyvus.elex.config.JwtProps;
import com.yettensyvus.elex.controller.DTO.request.LoginRequest;
import com.yettensyvus.elex.controller.DTO.response.AuthResponse;
import com.yettensyvus.elex.domain.Seller;
import com.yettensyvus.elex.domain.constants.ACCOUNT_STATUS;
import com.yettensyvus.elex.repository.VerificationCodeRepository;
import com.yettensyvus.elex.service.AuthService;
import com.yettensyvus.elex.service.EmailService;
import com.yettensyvus.elex.service.SellerReportService;
import com.yettensyvus.elex.service.SellerService;
import com.yettensyvus.elex.domain.*;
import com.yettensyvus.elex.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final VerificationCodeRepository verificationCodeRepository;
    private final SellerService sellerService;
    private final AuthService authService;
    private final EmailService emailService;
    private final SellerReportService sellerReportService;
    private final JwtProps jwtProps;
    private final CorsProps corsProps;

    private Seller getSellerFromJwt(String jwt) throws Exception {
        return sellerService.getSellerProfile(jwt);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {
        req.setEmail("seller_" + req.getEmail());
        AuthResponse authResponse = authService.signIn(req);
        return ResponseEntity.ok(authResponse);
    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("Invalid OTP");
        }

        Seller seller = sellerService.verifyEmail(verificationCode.getEmailAddress(), otp);
        return ResponseEntity.ok(seller);
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception {
        Seller savedSeller = sellerService.createSeller(seller);

        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmailAddress(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject = "Elex Email Verification Code";
        String text = "Welcome to Elex. Verify your account using the following link: ";
        String frontendUrl = corsProps.getAllowedOrigins() +"/verify-seller/";

        emailService.sendVerificationOtpEmail(seller.getEmail(), otp, subject, text + frontendUrl);
        return ResponseEntity.status(201).body(savedSeller);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception {
        Seller seller = sellerService.getSellerById(id);
        return ResponseEntity.ok(seller);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader Map<String, String> headers) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        Seller seller = getSellerFromJwt(jwtHeaderValue);
        return ResponseEntity.ok(seller);
    }

    @GetMapping("/report")
    public ResponseEntity<SellerReport> getSellerReport(@RequestHeader Map<String, String> headers) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        Seller seller = getSellerFromJwt(jwtHeaderValue);
        SellerReport report = sellerReportService.getSellerReport(seller);
        return ResponseEntity.ok(report);
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false) ACCOUNT_STATUS status) {
        List<Seller> sellers = sellerService.getAllSellers(status);
        return ResponseEntity.ok(sellers);
    }

    @PatchMapping
    public ResponseEntity<Seller> updateSeller(@RequestHeader Map<String, String> headers,
                                               @RequestBody Seller seller) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        Seller profile = getSellerFromJwt(jwtHeaderValue);
        Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}
