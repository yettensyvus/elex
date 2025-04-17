package com.yettensyvus.elex.repository;

import com.yettensyvus.elex.domain.Seller;
import com.yettensyvus.elex.domain.constants.ACCOUNT_STATUS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(ACCOUNT_STATUS status);
}
