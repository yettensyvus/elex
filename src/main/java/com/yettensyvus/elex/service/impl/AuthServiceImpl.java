package com.yettensyvus.elex.service.impl;

import com.yettensyvus.elex.config.JwtProvider;
import com.yettensyvus.elex.controller.DTO.request.LoginRequest;
import com.yettensyvus.elex.controller.DTO.request.SignupRequest;
import com.yettensyvus.elex.controller.DTO.response.AuthResponse;
import com.yettensyvus.elex.domain.Cart;
import com.yettensyvus.elex.domain.User;
import com.yettensyvus.elex.domain.VerificationCode;
import com.yettensyvus.elex.domain.constants.USER_ROLE;
import com.yettensyvus.elex.repository.CartRepository;
import com.yettensyvus.elex.repository.SellerRepository;
import com.yettensyvus.elex.repository.UserRepository;
import com.yettensyvus.elex.repository.VerificationCodeRepository;
import com.yettensyvus.elex.service.AuthService;
import com.yettensyvus.elex.service.EmailService;
import com.yettensyvus.elex.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String SIGNING_PREFIX = "signing_";
    private static final String SELLER_PREFIX = "seller_";

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserServiceImpl customUserService;
    private final SellerRepository sellerRepository;

    @Override
    public String createUser(SignupRequest req) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());
        if (verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())) {
            throw new Exception("Wrong OTP.");
        }

        User user = userRepository.findByEmail(req.getEmail());
        if (user == null) {
            user = new User();
            user.setEmail(req.getEmail());
            user.setFullName(req.getFullName());
            user.setRole(USER_ROLE.ROLE_CUSTOMER);
            user.setMobile("37379******");
            user.setPassword(passwordEncoder.encode(req.getOtp()));
            user = userRepository.save(user);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                req.getEmail(),
                null,
                Collections.singletonList(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    @Override
    public void sendLoginOtp(String email, USER_ROLE role) throws Exception {
        if (email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());
            validateUserOrSellerExists(email, role);
        }

        VerificationCode existingCode = verificationCodeRepository.findByEmail(email);
        if (existingCode != null) {
            verificationCodeRepository.delete(existingCode);
        }

        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        emailService.sendVerificationOtpEmail(
                email,
                otp,
                "Login/Signup OTP",
                "Your login/signup OTP is - " + otp
        );
    }

    @Override
    public AuthResponse signIn(LoginRequest request) throws Exception {
        String username = request.getEmail();
        String otp = request.getOtp();

        Authentication authentication = authenticate(username, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login success");

        String roleName = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);

        authResponse.setRole(USER_ROLE.valueOf(roleName));
        return authResponse;
    }

    private Authentication authenticate(String username, String otp) throws Exception {
        if (username.startsWith(SELLER_PREFIX)) {
            username = username.substring(SELLER_PREFIX.length());
        }

        UserDetails userDetails = customUserService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("Wrong OTP");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private void validateUserOrSellerExists(String email, USER_ROLE role) throws Exception {
        if (role == USER_ROLE.ROLE_SELLER) {
            if (sellerRepository.findByEmail(email) == null) {
                throw new Exception("Seller does not exist with provided email");
            }
        } else {
            if (userRepository.findByEmail(email) == null) {
                throw new Exception("User does not exist with provided email");
            }
        }
    }
}