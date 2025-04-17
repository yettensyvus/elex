package com.yettensyvus.elex.domain;

import com.yettensyvus.elex.domain.abstraction.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Deal extends BaseEntity {

    private Integer discount;

    @OneToOne(fetch = FetchType.LAZY)
    private HomeCategory category;
}