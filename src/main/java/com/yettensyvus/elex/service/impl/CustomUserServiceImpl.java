package com.yettensyvus.elex.service.impl;

import com.yettensyvus.elex.domain.Seller;
import com.yettensyvus.elex.domain.User;
import com.yettensyvus.elex.domain.constants.USER_ROLE;
import com.yettensyvus.elex.repository.SellerRepository;
import com.yettensyvus.elex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    public static final String SELLER_PREFIX = "seller_";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.startsWith(SELLER_PREFIX)) {
            return loadSellerByUsername(username);
        } else {
            return loadUserByEmail(username);
        }
    }

    private UserDetails loadSellerByUsername(String username) {
        String actualUsername = username.substring(SELLER_PREFIX.length());
        Seller seller = sellerRepository.findByEmail(actualUsername);

        if (seller != null) {
            return buildUserDetails(seller.getEmail(), seller.getPassword(), seller.getRole());
        }
        throw new UsernameNotFoundException("Seller not found with email: " + username);
    }

    private UserDetails loadUserByEmail(String username) {
        User user = userRepository.findByEmail(username);

        if (user != null) {
            return buildUserDetails(user.getEmail(), user.getPassword(), user.getRole());
        }
        throw new UsernameNotFoundException("User not found with email: " + username);
    }

    private UserDetails buildUserDetails(String email, String password, USER_ROLE role) {
        if (role == null) {
            role = USER_ROLE.ROLE_CUSTOMER;
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(role.toString());
        return new org.springframework.security.core.userdetails.User(email, password, Collections.singletonList(authority));
    }
}
