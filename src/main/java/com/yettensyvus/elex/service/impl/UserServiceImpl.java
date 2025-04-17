package com.yettensyvus.elex.service.impl;

import com.yettensyvus.elex.config.JwtProvider;
import com.yettensyvus.elex.domain.User;
import com.yettensyvus.elex.repository.UserRepository;
import com.yettensyvus.elex.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User findByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return this.findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("user not found with email - " + email);
        }
        return user;
    }
}
