package com.yettensyvus.elex.service.impl;

import com.yettensyvus.elex.controller.DTO.request.CreateProductRequest;
import com.yettensyvus.elex.domain.Category;
import com.yettensyvus.elex.domain.Product;
import com.yettensyvus.elex.domain.Seller;
import com.yettensyvus.elex.repository.CategoryRepository;
import com.yettensyvus.elex.repository.ProductRepository;
import com.yettensyvus.elex.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) throws IllegalAccessException {
        Category category3 = getCategoryChain(req.getCategory(), req.getCategory2(), req.getCategory3());

        int discountPercentage = calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(req.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(req.getTitle());
        product.setSellingPrice(req.getSellingPrice());
        product.setImages(req.getImages());
        product.setMrpPrice(req.getMrpPrice());
        product.setDiscountPercent(discountPercentage);

        return productRepository.save(product);
    }

    private Category getCategoryChain(String category1Id, String category2Id, String category3Id) {
        Category category1 = getOrCreateCategory(category1Id, 1, null);
        Category category2 = getOrCreateCategory(category2Id, 2, category1);
        return getOrCreateCategory(category3Id, 3, category2);
    }

    private Category getOrCreateCategory(String categoryId, int level, Category parentCategory) {
        Category category = categoryRepository.findByCategoryId(categoryId);
        if (category == null) {
            category = new Category();
            category.setCategoryId(categoryId);
            category.setLevel(level);
            category.setParentCategory(parentCategory);
            categoryRepository.save(category);
        }
        return category;
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) throws IllegalAccessException {
        if (mrpPrice < 0) {
            throw new IllegalAccessException("Actual price must be greater than 0");
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int) discountPercentage;
    }

    @Override
    public void deleteProduct(Long productId) throws Exception {
        Product product = findProductById(productId);
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws Exception {
        findProductById(productId);
        product.setId(productId);
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws Exception {
        return productRepository.findById(productId)
                .orElseThrow(() -> new Exception("Product not found with id: " + productId));
    }

    @Override
    public List<Product> searchProduct(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand,
                                        Integer minPrice, Integer maxPrice, Integer minDiscount, String sort,
                                        String stock, Integer pageNumber) {
        Specification<Product> spec = buildProductSpecification(category, brand, minPrice, maxPrice, minDiscount, stock);

        Pageable pageable = buildPageRequest(sort, pageNumber);

        return productRepository.findAll(spec, pageable);
    }

    private Specification<Product> buildProductSpecification(String category, String brand,
                                                             Integer minPrice, Integer maxPrice, Integer minDiscount, String stock) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (category != null) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }
            if (minDiscount != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
            }
            if (stock != null) {
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageRequest(String sort, Integer pageNumber) {
        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "price_low":
                    return PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("sellingPrice").ascending());
                case "price_high":
                    return PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("sellingPrice").descending());
                default:
                    return PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
            }
        } else {
            return PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
        }
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}
