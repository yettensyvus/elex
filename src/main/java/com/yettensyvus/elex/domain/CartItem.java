package com.yettensyvus.elex.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yettensyvus.elex.domain.abstraction.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CartItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Cart cart;

    @OneToOne(fetch = FetchType.LAZY)
    private Product product;

    private int quantity = 1;

    private Integer mrpPrice;
    private Integer sellingPrice;

    private Long userId;
}
