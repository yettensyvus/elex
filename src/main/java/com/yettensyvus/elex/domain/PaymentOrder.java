package com.yettensyvus.elex.domain;

import com.yettensyvus.elex.domain.abstraction.BaseEntity;
import com.yettensyvus.elex.domain.constants.PAYMENT_METHOD;
import com.yettensyvus.elex.domain.constants.PAYMENT_ORDER_STATUS;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "payment_orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentOrder extends BaseEntity {

    private Long amount;

    private PAYMENT_ORDER_STATUS status = PAYMENT_ORDER_STATUS.PENDING;
    private PAYMENT_METHOD paymentMethod;

    private String paymentLinkId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "paymentOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Order> orders = new HashSet<>();

}
