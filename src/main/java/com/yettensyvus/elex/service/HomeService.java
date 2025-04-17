package com.yettensyvus.elex.service;

import com.yettensyvus.elex.domain.Home;
import com.yettensyvus.elex.domain.HomeCategory;

import java.util.List;

public interface HomeService {
    Home createHomePageData(List<HomeCategory> allCategory);
}