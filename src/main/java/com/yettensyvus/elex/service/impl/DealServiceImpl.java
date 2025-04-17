package com.yettensyvus.elex.service.impl;

import com.yettensyvus.elex.domain.Deal;
import com.yettensyvus.elex.domain.HomeCategory;
import com.yettensyvus.elex.repository.DealRepository;
import com.yettensyvus.elex.repository.HomeCategoryRepository;
import com.yettensyvus.elex.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private final HomeCategoryRepository homeCategoryRepository;
    private final DealRepository dealRepository;

    @Override
    public List<Deal> getDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) throws Exception {
        HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId())
                .orElseThrow(() -> new Exception("Category not found"));

        deal.setCategory(category);
        deal.setDiscount(deal.getDiscount());

        return dealRepository.save(deal);
    }

    @Override
    public Deal updateDeal(Deal deal, Long id) throws Exception {
        Deal existingDeal = dealRepository.findById(id)
                .orElseThrow(() -> new Exception("Deal not found"));

        HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId())
                .orElseThrow(() -> new Exception("Category not found"));

        existingDeal.setDiscount(deal.getDiscount());
        existingDeal.setCategory(category);

        return dealRepository.save(existingDeal);
    }

    @Override
    public void deleteDeal(Long id) throws Exception {
        Deal deal = dealRepository.findById(id)
                .orElseThrow(() -> new Exception("Deal not found"));

        dealRepository.delete(deal);
    }
}
