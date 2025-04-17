package com.yettensyvus.elex.domain;

import com.yettensyvus.elex.domain.abstraction.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seller_reports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SellerReport extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private Seller seller;

    private Long totalEarnings = 0L;
    private Long totalSales = 0L;
    private Long totalRefunds = 0L;
    private Long totalTax = 0L;
    private Long netEarnings = 0L;

    private Integer totalOrders = 0;
    private Integer canceledOrders = 0;
    private Integer totalTransactions = 0;
}
