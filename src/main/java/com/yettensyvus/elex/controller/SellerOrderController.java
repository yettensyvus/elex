package com.yettensyvus.elex.controller;

import com.yettensyvus.elex.config.JwtProps;
import com.yettensyvus.elex.domain.Order;
import com.yettensyvus.elex.domain.Seller;
import com.yettensyvus.elex.domain.constants.ORDER_STATUS;
import com.yettensyvus.elex.service.OrderService;
import com.yettensyvus.elex.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seller/orders")
@RequiredArgsConstructor
public class SellerOrderController {

    private final OrderService orderService;
    private final SellerService sellerService;
    private final JwtProps jwtProps;

    @GetMapping("/user")
    public ResponseEntity<List<Order>> getAllOrdersHandler(@RequestHeader Map<String, String> headers) {
        try {
            String jwtHeaderValue = headers.get(jwtProps.getHeader());
            Seller seller = sellerService.getSellerProfile(jwtHeaderValue);
            List<Order> orders = orderService.sellersOrder(seller.getId());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrderHandler(@PathVariable Long orderId,
                                                    @PathVariable ORDER_STATUS orderStatus,
                                                    @RequestHeader Map<String, String> headers) {
        try {
            Order order = orderService.updateOrderStatus(orderId, orderStatus);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }
}
