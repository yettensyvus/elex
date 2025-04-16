package com.yettensyvus.elex.domain;

import com.yettensyvus.elex.domain.abstraction.BaseProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseProductEntity {

    private int mrpPrice;
    private int sellingPrice;
    private int discountPercent;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> images = new ArrayList<>();

    private int numRatings;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    private Seller seller;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}