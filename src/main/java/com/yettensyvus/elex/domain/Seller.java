package com.yettensyvus.elex.domain;

import com.yettensyvus.elex.domain.abstraction.BasePerson;
import com.yettensyvus.elex.domain.constants.ACCOUNT_STATUS;
import com.yettensyvus.elex.domain.constants.USER_ROLE;
import com.yettensyvus.elex.domain.details.BankDetails;
import com.yettensyvus.elex.domain.details.BusinessDetails;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Seller extends BasePerson {

    private String sellerName;

    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();

    @Embedded
    private BankDetails bankDetails = new BankDetails();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address pickupAddress = new Address();

    private String TIN; // Tax Code of the Republic of Moldova

    private boolean isEmailVerified = false;

    private ACCOUNT_STATUS accountStatus = ACCOUNT_STATUS.PENDING_VERIFICATION;

    @PrePersist
    public void assignSellerRole() {
        this.setRole(USER_ROLE.ROLE_SELLER);
    }
}
