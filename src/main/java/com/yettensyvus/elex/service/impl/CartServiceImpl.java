package com.yettensyvus.elex.service.impl;

import com.yettensyvus.elex.domain.Cart;
import com.yettensyvus.elex.domain.CartItem;
import com.yettensyvus.elex.domain.Product;
import com.yettensyvus.elex.domain.User;
import com.yettensyvus.elex.repository.CartItemRepository;
import com.yettensyvus.elex.repository.CartRepository;
import com.yettensyvus.elex.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addCartItem(User user, Product product, int quantity) throws IllegalAccessException {
        Cart cart = findUserCart(user);

        CartItem existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);
        if (existingCartItem == null) {
            return createAndSaveNewCartItem(cart, user, product, quantity);
        }

        return existingCartItem;
    }

    @Override
    public Cart findUserCart(User user) throws IllegalAccessException {
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            throw new IllegalAccessException("Cart not found for user with ID: " + user.getId());
        }

        // Calculate total prices and item counts
        calculateCartTotals(cart);

        return cart;
    }

    private void calculateCartTotals(Cart cart) {
        int totalMrpPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItemCount = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalMrpPrice += cartItem.getMrpPrice();
            totalDiscountedPrice += cartItem.getSellingPrice();
            totalItemCount += cartItem.getQuantity();
        }

        cart.setTotalMrpPrice(totalMrpPrice);
        cart.setTotalSellingPrice(totalDiscountedPrice);
        cart.setTotalItem(totalItemCount);
        cart.setDiscount(calculateDiscountPercentage(totalMrpPrice, totalDiscountedPrice));
    }

    private CartItem createAndSaveNewCartItem(Cart cart, User user, Product product, int quantity) {
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setUserId(user.getId());

        // Calculate prices
        int sellingPrice = quantity * product.getSellingPrice();
        int mrpPrice = quantity * product.getMrpPrice();

        newCartItem.setSellingPrice(sellingPrice);
        newCartItem.setMrpPrice(mrpPrice);

        // Add to cart and persist
        cart.getCartItems().add(newCartItem);
        newCartItem.setCart(cart);

        return cartItemRepository.save(newCartItem);
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if (mrpPrice <= 0) {
            return 0; // Avoid division by zero or negative values
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int) discountPercentage;
    }
}
