package com.yettensyvus.elex.domain;

import com.yettensyvus.elex.domain.abstraction.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntity {

    private String name;

    @Column(unique = true, nullable = false)
    private String categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category parentCategory;

    @NotNull
    private Integer level;
}
