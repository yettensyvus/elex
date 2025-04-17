package com.yettensyvus.elex.controller;
import com.yettensyvus.elex.config.JwtProps;
import com.yettensyvus.elex.controller.DTO.request.CreateReviewRequest;
import com.yettensyvus.elex.controller.DTO.response.ApiResponse;
import com.yettensyvus.elex.domain.Product;
import com.yettensyvus.elex.domain.Review;
import com.yettensyvus.elex.domain.User;
import com.yettensyvus.elex.service.ProductService;
import com.yettensyvus.elex.service.ReviewService;
import com.yettensyvus.elex.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;
    private final JwtProps jwtProps;

    private User getUserFromJwt(String jwt) throws Exception {
        return userService.findByJwtToken(jwt);
    }

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<Review> writeReview(@RequestBody CreateReviewRequest req,
                                              @PathVariable Long productId,
                                              @RequestHeader Map<String, String> headers) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        User user = getUserFromJwt(jwtHeaderValue);
        Product product = productService.findProductById(productId);
        Review review = reviewService.createReview(req, user, product);

        return ResponseEntity.ok(review);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(@RequestBody CreateReviewRequest req,
                                               @PathVariable Long reviewId,
                                               @RequestHeader Map<String, String> headers) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        User user = getUserFromJwt(jwtHeaderValue);
        Review updatedReview = reviewService.updateReview(reviewId, req.getReviewText(), req.getReviewRating(), user.getId());
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId,
                                                    @RequestHeader Map<String, String> headers) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        User user = getUserFromJwt(jwtHeaderValue);
        reviewService.deleteReview(reviewId, user.getId());

        ApiResponse response = new ApiResponse();
        response.setMessage("Review deleted successfully");
        return ResponseEntity.ok(response);
    }
}
