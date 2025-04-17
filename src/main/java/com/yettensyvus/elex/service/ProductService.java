package com.yettensyvus.elex.service;

import com.yettensyvus.elex.controller.DTO.request.CreateProductRequest;
import com.yettensyvus.elex.domain.Product;
import com.yettensyvus.elex.domain.Seller;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Product createProduct(CreateProductRequest req, Seller seller) throws IllegalAccessException;

    void deleteProduct(Long productId) throws Exception;

    Product updateProduct(Long productId, Product product) throws Exception;

    Product findProductById(Long productId) throws Exception;

    List<Product> searchProduct(String query);

    Page<Product> getAllProducts(
            String category,
            String brand,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber
    );

    List<Product> getProductBySellerId(Long sellerId);
}

