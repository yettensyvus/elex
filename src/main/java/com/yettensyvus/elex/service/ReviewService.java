package com.yettensyvus.elex.service;

import com.yettensyvus.elex.controller.DTO.request.CreateReviewRequest;
import com.yettensyvus.elex.domain.Product;
import com.yettensyvus.elex.domain.Review;
import com.yettensyvus.elex.domain.User;

import java.util.List;

public interface ReviewService {
    Review createReview(CreateReviewRequest request, User user, Product product);

    List<Review> getReviewByProductId(Long productId);

    Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception;

    void deleteReview(Long reviewId, Long userId) throws Exception;

    Review getReviewById(Long reviewId) throws Exception;
}