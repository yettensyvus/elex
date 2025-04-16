package com.yettensyvus.elex.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Home {

    private List<HomeCategory> grid = new ArrayList<>();
    private List<HomeCategory> shopByCategories = new ArrayList<>();
    private List<HomeCategory> electronicCategories = new ArrayList<>();
    private List<HomeCategory> dealCategories = new ArrayList<>();
    private List<Deal> deals = new ArrayList<>();
}
