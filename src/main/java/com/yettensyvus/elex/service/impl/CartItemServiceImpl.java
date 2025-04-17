package com.yettensyvus.elex.service.impl;

import com.yettensyvus.elex.domain.CartItem;
import com.yettensyvus.elex.repository.CartItemRepository;
import com.yettensyvus.elex.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem updateCartItem(Long userId, Long itemId, CartItem updatedItem) throws Exception {
        CartItem existingItem = findCartItemById(itemId);
        validateCartItemOwnership(existingItem, userId);

        int quantity = updatedItem.getQuantity();
        existingItem.setQuantity(quantity);
        existingItem.setMrpPrice(quantity * existingItem.getProduct().getMrpPrice());
        existingItem.setSellingPrice(quantity * existingItem.getProduct().getSellingPrice());

        return cartItemRepository.save(existingItem);
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws Exception {
        CartItem cartItem = findCartItemById(cartItemId);
        validateCartItemOwnership(cartItem, userId);
        cartItemRepository.delete(cartItem);
    }

    @Override
    public CartItem findCartItemById(Long id) throws Exception {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new Exception("Cart item not found with ID: " + id));
    }

    private void validateCartItemOwnership(CartItem item, Long userId) throws Exception {
        Long ownerId = item.getCart().getUser().getId();
        if (!ownerId.equals(userId)) {
            throw new Exception("Unauthorized: This cart item doesn't belong to user with ID: " + userId);
        }
    }
}
