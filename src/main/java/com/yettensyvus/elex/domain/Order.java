package com.yettensyvus.elex.domain;

import com.yettensyvus.elex.domain.abstraction.BaseEntity;
import com.yettensyvus.elex.domain.constants.ORDER_STATUS;
import com.yettensyvus.elex.domain.constants.PAYMENT_STATUS;
import com.yettensyvus.elex.domain.details.PaymentDetails;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "orders")
public class Order extends BaseEntity {

    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Long sellerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "payment_order_id")
    private PaymentOrder paymentOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    private Address shippingAddress;

    @Embedded
    private PaymentDetails paymentDetails = new PaymentDetails();

    private double totalMrpPrice;
    private Integer totalSellingPrice;
    private Integer discount;

    private ORDER_STATUS orderStatus;

    private int totalItem;

    private PAYMENT_STATUS paymentStatus = PAYMENT_STATUS.PENDING;

    private LocalDateTime orderDate = LocalDateTime.now();
    private LocalDateTime deliverDate = orderDate.plusDays(7);

}
