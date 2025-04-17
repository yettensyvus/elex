package com.yettensyvus.elex.controller;

import com.yettensyvus.elex.config.JwtProps;
import com.yettensyvus.elex.controller.DTO.request.AddItemRequest;
import com.yettensyvus.elex.controller.DTO.response.ApiResponse;
import com.yettensyvus.elex.domain.Cart;
import com.yettensyvus.elex.domain.CartItem;
import com.yettensyvus.elex.domain.Product;
import com.yettensyvus.elex.domain.User;
import com.yettensyvus.elex.service.CartItemService;
import com.yettensyvus.elex.service.CartService;
import com.yettensyvus.elex.service.ProductService;
import com.yettensyvus.elex.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;
    private final JwtProps jwtProps;


    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(
            @RequestHeader Map<String, String> headers
    ) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        User user = userService.findByJwtToken(jwtHeaderValue);
        Cart cart = cartService.findUserCart(user);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(
            @RequestBody AddItemRequest request,
            @RequestHeader Map<String, String> headers
    ) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        User user = userService.findByJwtToken(jwtHeaderValue);
        Product product = productService.findProductById(request.getProductId());

        cartService.addCartItem(user, product, request.getQuantity());

        ApiResponse response = new ApiResponse();
        response.setMessage("Item added to cart successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestHeader Map<String, String> headers
    ) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        User user = userService.findByJwtToken(jwtHeaderValue);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResponse response = new ApiResponse();
        response.setMessage("Item removed from cart successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem,
            @RequestHeader Map<String, String> headers
    ) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        User user = userService.findByJwtToken(jwtHeaderValue);

        if (cartItem.getQuantity() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        CartItem updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedCartItem);
    }
}
