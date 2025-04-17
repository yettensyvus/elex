package com.yettensyvus.elex.service.impl;

import com.yettensyvus.elex.domain.Deal;
import com.yettensyvus.elex.domain.Home;
import com.yettensyvus.elex.domain.HomeCategory;
import com.yettensyvus.elex.domain.constants.HOME_CATEGORY_SECTION;
import com.yettensyvus.elex.repository.DealRepository;
import com.yettensyvus.elex.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final DealRepository dealRepository;

    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {
        List<HomeCategory> gridCategories = filterCategoriesBySection(allCategories, HOME_CATEGORY_SECTION.GRID);
        List<HomeCategory> shopByCategories = filterCategoriesBySection(allCategories, HOME_CATEGORY_SECTION.SHOP_BY_CATEGORIES);
        List<HomeCategory> electronicCategories = filterCategoriesBySection(allCategories, HOME_CATEGORY_SECTION.IT_CATEGORIES);
        List<HomeCategory> dealCategories = filterCategoriesBySection(allCategories, HOME_CATEGORY_SECTION.DEALS);

        List<Deal> createDeals = getOrCreateDeals(dealCategories);

        Home home = new Home();
        home.setGrid(gridCategories);
        home.setShopByCategories(shopByCategories);
        home.setElectronicCategories(electronicCategories);
        home.setDeals(createDeals);
        home.setDealCategories(dealCategories);

        return home;
    }

    private List<HomeCategory> filterCategoriesBySection(List<HomeCategory> allCategories, HOME_CATEGORY_SECTION section) {
        return allCategories.stream()
                .filter(category -> category.getSection() == section)
                .collect(Collectors.toList());
    }

    private List<Deal> getOrCreateDeals(List<HomeCategory> dealCategories) {
        List<Deal> existingDeals = dealRepository.findAll();
        if (existingDeals.isEmpty()) {
            return dealCategories.stream()
                    .map(category -> new Deal(10, category))
                    .collect(Collectors.toList());
        }
        return existingDeals;
    }
}
