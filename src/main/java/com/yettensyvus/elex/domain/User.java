package com.yettensyvus.elex.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yettensyvus.elex.domain.abstraction.BasePerson;
import com.yettensyvus.elex.domain.constants.USER_ROLE;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BasePerson {

    private String fullName;

    @OneToMany
    private Set<Address> addresses = new HashSet<>();

    @ManyToMany
    @JsonIgnore
    private Set<Coupon> usedCoupons = new HashSet<>();

    @PrePersist
    public void assignDefaultRole() {
        if (this.getRole() == null) {
            this.setRole(USER_ROLE.ROLE_CUSTOMER);
        }
    }
}
