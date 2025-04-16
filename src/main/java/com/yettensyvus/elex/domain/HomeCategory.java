package com.yettensyvus.elex.domain;

import com.yettensyvus.elex.domain.abstraction.BaseEntity;
import com.yettensyvus.elex.domain.constants.HOME_CATEGORY_SECTION;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HomeCategory extends BaseEntity {

    private String name;
    private String image;
    private String categoryId;
    private HOME_CATEGORY_SECTION section;
}
