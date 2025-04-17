package com.yettensyvus.elex.service;

import com.yettensyvus.elex.domain.Seller;
import com.yettensyvus.elex.domain.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);

    SellerReport updateSellerReport(SellerReport sellerReport);
}