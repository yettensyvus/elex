package com.yettensyvus.elex.controller;

import com.yettensyvus.elex.domain.Home;
import com.yettensyvus.elex.domain.HomeCategory;
import com.yettensyvus.elex.service.HomeCategoryService;
import com.yettensyvus.elex.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/home-categories")
public class HomeCategoryController {

    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;

    @PostMapping
    public ResponseEntity<Home> createHomeCategories(@RequestBody List<HomeCategory> homeCategories) {
        List<HomeCategory> createdCategories = homeCategoryService.createCategories(homeCategories);
        Home homeData = homeService.createHomePageData(createdCategories);
        return new ResponseEntity<>(homeData, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<HomeCategory>> getAllHomeCategories() {
        List<HomeCategory> categories = homeCategoryService.getAllHomeCategories();
        return ResponseEntity.ok(categories);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(@PathVariable Long id,
                                                           @RequestBody HomeCategory homeCategory) throws Exception {
        HomeCategory updatedCategory = homeCategoryService.updateHomeCategory(homeCategory, id);
        return ResponseEntity.ok(updatedCategory);
    }
}
