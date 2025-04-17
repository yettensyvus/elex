package com.yettensyvus.elex.service.impl;

import com.yettensyvus.elex.domain.Cart;
import com.yettensyvus.elex.domain.Coupon;
import com.yettensyvus.elex.domain.User;
import com.yettensyvus.elex.repository.CartRepository;
import com.yettensyvus.elex.repository.CouponRepository;
import com.yettensyvus.elex.repository.UserRepository;
import com.yettensyvus.elex.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public Cart applyCoupon(String code, double orderValue, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        if (coupon == null) {
            throw new Exception("Coupon code is invalid or does not exist.");
        }

        Cart cart = cartRepository.findByUserId(user.getId());

        if (user.getUsedCoupons().contains(coupon)) {
            throw new Exception("Coupon has already been used by the user.");
        }

        if (orderValue < coupon.getMinimumOrderValue()) {
            throw new Exception("Coupon requires a minimum order value of " + coupon.getMinimumOrderValue());
        }

        if (isCouponValid(coupon)) {
            applyCouponToCart(coupon, cart);
            user.getUsedCoupons().add(coupon);
            userRepository.save(user);
            cartRepository.save(cart);
            return cart;
        }

        throw new Exception("Coupon is not active or has expired.");
    }

    @Override
    public Cart removeCoupon(String code, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        if (coupon == null) {
            throw new Exception("Coupon not found.");
        }

        Cart cart = cartRepository.findByUserId(user.getId());
        double discountAmount = calculateDiscount(cart, coupon);
        cart.setTotalSellingPrice(cart.getTotalSellingPrice() + discountAmount);
        cart.setCouponCode(null);

        return cartRepository.save(cart);
    }

    @Override
    public Coupon findCouponById(Long id) throws Exception {
        return couponRepository.findById(id).orElseThrow(() ->
                new Exception("Coupon not found with ID: " + id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupon() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCoupon(Long id) throws Exception {
        findCouponById(id);
        couponRepository.deleteById(id);
    }

    private boolean isCouponValid(Coupon coupon) {
        LocalDate today = LocalDate.now();
        return coupon.isActive() && !today.isBefore(coupon.getValidityStartDate()) && !today.isAfter(coupon.getValidityEndDate());
    }

    private void applyCouponToCart(Coupon coupon, Cart cart) {
        double discountPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100;
        cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountPrice);
        cart.setCouponCode(coupon.getCode());
    }

    private double calculateDiscount(Cart cart, Coupon coupon) {
        return (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100;
    }
}
