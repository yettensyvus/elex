package com.yettensyvus.elex.service;

import com.yettensyvus.elex.domain.Cart;
import com.yettensyvus.elex.domain.CartItem;
import com.yettensyvus.elex.domain.Product;
import com.yettensyvus.elex.domain.User;

public interface CartService {
    CartItem addCartItem(User user, Product product, int quantity) throws IllegalAccessException;

    Cart findUserCart(User user) throws IllegalAccessException;
}