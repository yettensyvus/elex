package com.yettensyvus.elex.service;

import com.yettensyvus.elex.domain.Order;
import com.yettensyvus.elex.domain.PaymentOrder;
import com.yettensyvus.elex.domain.User;

import java.util.Set;

public interface PaymentService {
    PaymentOrder createOrder(User user, Set<Order> orders);

    PaymentOrder getPaymentOrderById(Long orderId) throws Exception;

    PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception;

    //TO DO
}
