package com.yettensyvus.elex.controller;

import com.yettensyvus.elex.config.JwtProps;
import com.yettensyvus.elex.controller.DTO.response.PaymentLinkResponse;
import com.yettensyvus.elex.domain.*;
import com.yettensyvus.elex.domain.constants.PAYMENT_METHOD;
import com.yettensyvus.elex.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final JwtProps jwtProps;

    @PostMapping
    public ResponseEntity<PaymentLinkResponse> createOrder(
            @RequestBody Address shippingAddress,
            @RequestParam PAYMENT_METHOD paymentMethod,
            @RequestHeader Map<String, String> headers
    ) throws Exception {
            //TO DO
        return null;
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> getUserOrders(@RequestHeader Map<String, String> headers) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        User user = userService.findByJwtToken(jwtHeaderValue);
        List<Order> orders = orderService.usersOrderHistory(user.getId());
        return ResponseEntity.accepted().body(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long orderId,
            @RequestHeader Map<String, String> headers
    ) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        userService.findByJwtToken(jwtHeaderValue);
        Order order = orderService.findOrderById(orderId);
        return ResponseEntity.accepted().body(order);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(
            @PathVariable Long orderItemId,
            @RequestHeader Map<String, String> headers
    ) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        userService.findByJwtToken(jwtHeaderValue);
        OrderItem orderItem = orderService.getOrderItemById(orderItemId);
        return ResponseEntity.accepted().body(orderItem);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long orderId,
            @RequestHeader Map<String, String> headers
    ) throws Exception {
        String jwtHeaderValue = headers.get(jwtProps.getHeader());
        User user = userService.findByJwtToken(jwtHeaderValue);
        Order order = orderService.cancelOrder(orderId, user);

        Seller seller = sellerService.getSellerById(order.getSellerId());
        SellerReport report = sellerReportService.getSellerReport(seller);

        report.setCanceledOrders(report.getCanceledOrders() + 1);
        report.setTotalRefunds(report.getTotalRefunds() + order.getTotalSellingPrice());

        sellerReportService.updateSellerReport(report);

        return ResponseEntity.ok(order);
    }
}
