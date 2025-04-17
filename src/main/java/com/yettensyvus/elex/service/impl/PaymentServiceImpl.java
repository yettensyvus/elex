package com.yettensyvus.elex.service.impl;


import com.yettensyvus.elex.domain.Order;
import com.yettensyvus.elex.domain.PaymentOrder;
import com.yettensyvus.elex.domain.User;
import com.yettensyvus.elex.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Override
    public PaymentOrder createOrder(User user, Set<Order> orders) {
        return null;
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long orderId) throws Exception {
        return null;
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception {
        return null;
    }

    //TO DO
}
