package com.yettensyvus.elex.repository;

import com.yettensyvus.elex.domain.Cart;
import com.yettensyvus.elex.domain.CartItem;
import com.yettensyvus.elex.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndProduct(Cart cart, Product product);

}
