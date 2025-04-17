package com.yettensyvus.elex.domain;

import com.yettensyvus.elex.domain.abstraction.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VerificationCode extends BaseEntity {

    private String otp;

    @Column(nullable = false)
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private Seller seller;
}
