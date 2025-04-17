package com.yettensyvus.elex.service;

import com.yettensyvus.elex.domain.User;

public interface UserService {
    User findByJwtToken(String jwt) throws Exception;

    User findUserByEmail(String email) throws Exception;
}