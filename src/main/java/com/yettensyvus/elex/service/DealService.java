package com.yettensyvus.elex.service;

import com.yettensyvus.elex.domain.Deal;

import java.util.List;

public interface DealService {
    List<Deal> getDeals();

    Deal createDeal(Deal deal);

    Deal updateDeal(Deal deal, Long id) throws Exception;

    void deleteDeal(Long id) throws Exception;
}