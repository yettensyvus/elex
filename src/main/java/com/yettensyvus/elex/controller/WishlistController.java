package com.yettensyvus.elex.controller;

import com.yettensyvus.elex.config.JwtProps;
import com.yettensyvus.elex.domain.Product;
import com.yettensyvus.elex.domain.User;
import com.yettensyvus.elex.domain.Wishlist;
import com.yettensyvus.elex.service.ProductService;
import com.yettensyvus.elex.service.UserService;
import com.yettensyvus.elex.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserService userService;
    private final ProductService productService;
    private final JwtProps jwtProps;

    @GetMapping
    public ResponseEntity<Wishlist> getWishlist(@RequestHeader Map<String, String> headers) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        User user = userService.findByJwtToken(jwtHeaderValue);
        Wishlist wishlist = wishlistService.getWishlistByUserId(user);
        return new ResponseEntity<>(wishlist, HttpStatus.OK);
    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<Wishlist> addProductToWishlist(@PathVariable Long productId,
                                                         @RequestHeader Map<String, String> headers) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        User user = userService.findByJwtToken(jwtHeaderValue);
        Product product = productService.findProductById(productId);
        Wishlist updatedWishlist = wishlistService.addProductToWishlist(user, product);
        return new ResponseEntity<>(updatedWishlist, HttpStatus.OK);
    }
}

