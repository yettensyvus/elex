package com.yettensyvus.elex.service;

import com.yettensyvus.elex.domain.Product;
import com.yettensyvus.elex.domain.User;
import com.yettensyvus.elex.domain.Wishlist;

public interface WishlistService {
    Wishlist createWishlist(User user);

    Wishlist getWishlistByUserId(User user);

    Wishlist addProductToWishlist(User user, Product product);
}
