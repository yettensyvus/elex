package com.yettensyvus.elex.controller;

import com.yettensyvus.elex.config.JwtProps;
import com.yettensyvus.elex.controller.DTO.request.CreateProductRequest;
import com.yettensyvus.elex.domain.Product;
import com.yettensyvus.elex.domain.Seller;
import com.yettensyvus.elex.service.SellerService;
import com.yettensyvus.elex.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sellers/products")
public class SellerProductController {

    private final SellerService sellerService;
    private final ProductService productService;
    private final JwtProps jwtProps;

    @GetMapping
    public ResponseEntity<List<Product>> getProductBySellerId(@RequestHeader Map<String, String> headers) {
        try {
            String jwtHeaderValue = headers.get(jwtProps.getHeader());
            Seller seller = sellerService.getSellerProfile(jwtHeaderValue);
            List<Product> products = productService.getProductBySellerId(seller.getId());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // Create new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request,
                                                 @RequestHeader Map<String, String> headers) {
        try {
            String jwtHeaderValue = headers.get(jwtProps.getHeader());
            Seller seller = sellerService.getSellerProfile(jwtHeaderValue);
            Product product = productService.createProduct(request, seller);
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,
                                                 @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(productId, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
