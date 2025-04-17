package com.yettensyvus.elex.service;

import com.yettensyvus.elex.domain.Cart;
import com.yettensyvus.elex.domain.Coupon;
import com.yettensyvus.elex.domain.User;

import java.util.List;

public interface CouponService {
    Cart applyCoupon(String code, double orderValue, User user) throws Exception;

    Cart removeCoupon(String code, User user) throws Exception;

    Coupon findCouponById(Long id) throws Exception;

    Coupon createCoupon(Coupon coupon);

    List<Coupon> findAllCoupon();

    void deleteCoupon(Long id) throws Exception;
}